package vistar.practice.demo.services.comment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vistar.practice.demo.dtos.comment.CommentLoadDto;
import vistar.practice.demo.mappers.CommentMapper;
import vistar.practice.demo.repositories.comment.CommentRepository;
import vistar.practice.demo.repositories.UserRepository;
import vistar.practice.demo.repositories.photo.PhotoRepository;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Transactional("transactionManager")
@Slf4j
public class CommentService {

    private final CommentRepository commentRepository;
    private final PhotoRepository photoRepository;
    private final UserRepository userRepository;

    private final CommentMapper commentMapper;

    public void save(
            CommentLoadDto commentLoadDto,
            String senderName
    ) {
        var commentEntity = commentMapper.toEntity(commentLoadDto);
        var photoId = commentLoadDto.getPhotoId();
        commentEntity.setPhoto(
                photoRepository.getReferenceById(photoId)
        );
        commentEntity.setUser(
                userRepository.findByUsername(senderName).orElseThrow(
                        () -> new NoSuchElementException("User (" + senderName + ") not found")
                )
        );
        commentRepository.save(commentEntity);
    }

    public void update(
            LocalDateTime lastCommentCreationTime,
            CommentLoadDto commentLoadDto,
            long commentOffset,
            String editorName
    ) {
        var commentEntity = commentRepository.findByOffset(
                lastCommentCreationTime,
                commentLoadDto.getPhotoId(),
                commentOffset
        ).orElseThrow(
                () -> new NoSuchElementException(
                        "Comment offset (" + commentOffset + ") not found on photo (id = " + commentLoadDto.getPhotoId() + ")"
                )
        );
        var editor = userRepository.findByUsername(editorName).orElseThrow(
                () -> new NoSuchElementException("User (" + editorName + ") not found")
        );
        if (!editor.getId().equals(commentEntity.getUser().getId())) {
            throw new RuntimeException("Editing of foreign comment is not allowed");
        }
        commentEntity.setText(commentLoadDto.getText());
    }

    public void delete(
            LocalDateTime lastCommentCreationTime,
            long photoId,
            long commentOffset,
            String username
    ) {
        var commentEntity = commentRepository.findByOffset(
                lastCommentCreationTime,
                photoId,
                commentOffset
        ).orElseThrow(
                () -> new NoSuchElementException(
                        "Comment offset (" + commentOffset + ") not found on photo (id = " + photoId + ")"
                )
        );
        var userId = userRepository.findByUsername(username).orElseThrow(
                () -> new NoSuchElementException("User (" + username + ") not found")
        ).getId();
        if (!userId.equals(commentEntity.getUser().getId())) {
            throw new RuntimeException("Deleting of foreign comment is not allowed");
        }
        commentEntity.setIsShown(false);
    }
}

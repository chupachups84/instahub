package vistar.practice.demo.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vistar.practice.demo.dtos.comment.CommentDto;
import vistar.practice.demo.mappers.CommentMapper;
import vistar.practice.demo.repositories.CommentRepository;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j

public class CommentService {

    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;

    public void save(CommentDto commentDto) {
        var commentEntity = commentMapper.toEntity(commentDto);
        commentRepository.save(commentEntity);
    }

    public CommentDto findById(long commentId) {
        var commentEntity = commentRepository.findById(commentId).orElseThrow(
                () -> new NoSuchElementException("Comment (id: " + commentId + ") does not exist")
        );
        return commentMapper.toDto(commentEntity);
    }

    public void update(long commentId, CommentDto commentDto) {
        var commentEntity = commentRepository.findById(commentId).orElseThrow(
                () -> new NoSuchElementException("Comment (id: " + commentId + ") does not exist")
        );
        commentMapper.updateFromDto(commentDto, commentEntity);
    }

    public void delete(long commentId) {

        if (!commentRepository.existsById(commentId)) {
            log.warn("Comment to delete (id: {}) does not exist", commentId);
        } else {
            commentRepository.deleteById(commentId);
        }
    }

}

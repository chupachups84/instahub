package vistar.practice.demo.services;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vistar.practice.demo.dtos.reaction.ReactionCreateEditDto;
import vistar.practice.demo.dtos.reaction.ReactionReadDto;
import vistar.practice.demo.mappers.ReactionMapper;
import vistar.practice.demo.models.ReactionsPhotosEntity;
import vistar.practice.demo.models.UserEntity;
import vistar.practice.demo.repositories.ReactionsPhotosRepository;
import vistar.practice.demo.repositories.UserRepository;
import vistar.practice.demo.repositories.photo.PhotoRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(transactionManager = "transactionManager")
public class ReactionPhotoService {
    private final PhotoRepository photoRepository;
    private final UserRepository userRepository;
    private final ReactionsPhotosRepository reactionsPhotosRepository;
    private final ReactionMapper mapper;

    @Transactional(transactionManager = "transactionManager", readOnly = true)
    public List<ReactionReadDto> getAllReactionsByPhotoId(Long photoId) {
        return reactionsPhotosRepository.findAllByPhoto(
                        photoRepository.findById(photoId).orElseThrow(
                                () -> new EntityNotFoundException("Photo not found")
                        )
                ).stream()
                .map(
                        s -> mapper.toReadDto(s.getReaction())
                ).toList();
    }


    public ReactionReadDto reactOnPhoto(Long photoId, ReactionCreateEditDto createEditDto, String username) {
        var photo = photoRepository.findById(photoId).orElseThrow(
                () -> new EntityNotFoundException("Photo not found")
        );
        var user = userRepository.findByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException("User not found")
        );
        var reaction = mapper.toEntity(createEditDto);
        ReactionsPhotosEntity savedReactionOnPhoto = reactionsPhotosRepository.save(
                ReactionsPhotosEntity.builder()
                        .reactionBy(user)
                        .photo(photo)
                        .reaction(reaction)
                        .build());
        return mapper.toReadDto(savedReactionOnPhoto.getReaction());
    }

    public void removeReactionFromPhoto(Long photoId, String username) {
        Long userId = userRepository.findByUsername(username)
                .map(UserEntity::getId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        ReactionsPhotosEntity reactionOnPhoto = reactionsPhotosRepository.findByReactionByAndPhoto(userId, photoId)
                .orElseThrow(() -> new EntityNotFoundException("reaction on photo not found"));
        reactionOnPhoto.setIsActive(false);
    }
}

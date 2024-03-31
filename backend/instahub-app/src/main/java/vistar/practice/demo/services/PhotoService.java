package vistar.practice.demo.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vistar.practice.demo.dtos.photo.PhotoDto;
import vistar.practice.demo.mappers.PhotoMapper;
import vistar.practice.demo.models.PhotoEntity;
import vistar.practice.demo.repositories.PhotoRepository;
import vistar.practice.demo.repositories.UserRepository;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Transactional("transactionManager")
@Slf4j
public class PhotoService {

    private final UserRepository userRepository;
    private final PhotoRepository photoRepository;

    public PhotoEntity save(PhotoDto photoDto) {

        final var userEntity = userRepository.findById(photoDto.getOwnerId()).orElseThrow(
                () -> new NoSuchElementException("User (id: " + photoDto.getOwnerId() + ") does not exist")
        );
        final var photoEntity = PhotoMapper.toEntity(photoDto);
        photoEntity.setUser(userEntity);

        return photoRepository.save(photoEntity);
    }

    public PhotoDto findById(long photoId) {

        var photoEntity = photoRepository.findById(photoId).orElseThrow(
                () -> new NoSuchElementException("Photo (id: " + photoId + ") does not exist")
        );
        return PhotoMapper.toDto(photoEntity);
    }

    public void update(long photoId, PhotoDto photoDto) {

        var photoEntity = photoRepository.findById(photoId).orElseThrow(
                () -> new NoSuchElementException("Photo (id: " + photoId + ") does not exist")
        );
        PhotoMapper.updateFromDto(photoDto, photoEntity);
    }

    public void delete(long photoId) {

        if (!photoRepository.existsById(photoId)) {
            log.warn("Photo to delete (id: " + photoId + ") does not exist");
        } else {
            photoRepository.deleteById(photoId);
        }
    }
}

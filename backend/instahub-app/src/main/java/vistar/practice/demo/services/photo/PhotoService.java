package vistar.practice.demo.services.photo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vistar.practice.demo.dtos.photo.PhotoInfoDto;
import vistar.practice.demo.mappers.PhotoMapper;
import vistar.practice.demo.models.photo.PhotoEntity;
import vistar.practice.demo.repositories.photo.PhotoRepository;
import vistar.practice.demo.repositories.UserRepository;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Transactional("transactionManager")
@Slf4j
public class PhotoService {

    private final UserRepository userRepository;
    private final PhotoRepository photoRepository;
    private final PhotoMapper photoMapper;

    public PhotoEntity save(PhotoInfoDto photoInfoDto) {

        final var userEntity = userRepository.findById(photoInfoDto.getOwnerId()).orElseThrow(
                () -> new NoSuchElementException("User (id: " + photoInfoDto.getOwnerId() + ") does not exist")
        );

        final var photoEntity = photoMapper.toEntity(photoInfoDto);
        photoEntity.setUser(userEntity);

        if (photoInfoDto.getIsAvatar() != null && photoInfoDto.getIsAvatar()) {
            demarkAvatar();
        }

        return photoRepository.save(photoEntity);
    }

    @Transactional(readOnly = true)
    public PhotoInfoDto findById(long photoId) {

        var photoEntity = photoRepository.findById(photoId).orElseThrow(
                () -> new NoSuchElementException("Photo (id: " + photoId + ") does not exist")
        );
        return photoMapper.toDto(photoEntity);
    }

    public void update(long photoId, PhotoInfoDto photoInfoDto) {

        var photoEntity = photoRepository.findById(photoId).orElseThrow(
                () -> new NoSuchElementException("Photo (id: " + photoId + ") does not exist")
        );
        photoMapper.updateFromDto(photoInfoDto, photoEntity);
    }

    public void delete(long photoId) {

        if (!photoRepository.existsById(photoId)) {
            log.warn("Photo to delete (id: " + photoId + ") does not exist");
        } else {
            photoRepository.deleteById(photoId);
        }
    }

    public void demarkAvatar() {

        PhotoEntity avatar = photoRepository.getByIsAvatarIsTrue();
        if (avatar != null) {
            avatar.setAvatar(false);
        }
    }
}
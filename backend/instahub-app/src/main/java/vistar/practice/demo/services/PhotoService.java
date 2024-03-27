package vistar.practice.demo.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vistar.practice.demo.dtos.photo.PhotoDto;
import vistar.practice.demo.mappers.PhotoMapper;
import vistar.practice.demo.models.PhotoEntity;
import vistar.practice.demo.repositories.PhotoRepository;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class PhotoService {

    private final PhotoRepository photoRepository;

    public void save(PhotoDto photoDto) {

        var photoEntity = PhotoMapper.toEntity(photoDto);
        photoRepository.save(photoEntity);
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

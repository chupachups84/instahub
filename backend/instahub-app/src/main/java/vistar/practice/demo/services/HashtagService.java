package vistar.practice.demo.services;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vistar.practice.demo.dtos.hashtag.HashtagCreateEditDto;
import vistar.practice.demo.dtos.hashtag.HashtagReadDto;
import vistar.practice.demo.mappers.HashtagMapper;
import vistar.practice.demo.models.PhotosHashtagsEntity;
import vistar.practice.demo.repositories.HashtagRepository;
import vistar.practice.demo.repositories.PhotosHashtagsRepository;
import vistar.practice.demo.repositories.photo.PhotoRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(transactionManager = "transactionManager")
public class HashtagService {
    private final HashtagRepository hashtagRepository;
    private final HashtagMapper hashtagMapper;
    private final PhotosHashtagsRepository photosHashtagsRepository;
    private final PhotoRepository photoRepository;

    @Transactional(transactionManager = "transactionManager", readOnly = true)
    public List<HashtagReadDto> findAll() {
        return hashtagRepository.findAll()
                .stream().map(hashtagMapper::toReadDto)
                .toList();
    }

    @Transactional(transactionManager = "transactionManager", readOnly = true)
    public Optional<HashtagReadDto> findById(Long id){
        return  hashtagRepository.findById(id)
                .map(hashtagMapper::toReadDto);
    }

    public HashtagReadDto create(HashtagCreateEditDto hashtagCreateEditDto) {

        var hashtagEntity = hashtagMapper.toEntity(hashtagCreateEditDto);
        hashtagRepository.save(hashtagEntity);
        photosHashtagsRepository.save(
                PhotosHashtagsEntity.builder()
                        .hashtag(hashtagEntity)
                        .photo(photoRepository.getReferenceById(hashtagCreateEditDto.getPhotoId()))
                        .build()
        );

        return hashtagMapper.toReadDto(hashtagEntity);
    }

    public Optional<HashtagReadDto> update(Long id ,HashtagCreateEditDto hashtagCreateEditDto) {
        return hashtagRepository.findById(id)
                .map((entity -> hashtagMapper.copyToEntityFromDto(entity, hashtagCreateEditDto)))
                .map(hashtagRepository::save)
                .map(hashtagMapper::toReadDto);
    }

    public boolean delete(Long id) {
        return hashtagRepository.findById(id)
                .map(hashtagEntity -> {
                    hashtagRepository.delete(hashtagEntity);
                    return true;
                })
                .orElse(false);
    }
}

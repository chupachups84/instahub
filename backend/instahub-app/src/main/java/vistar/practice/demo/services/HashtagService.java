package vistar.practice.demo.services;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vistar.practice.demo.dto.HashtagCreateEditDto;
import vistar.practice.demo.dto.HashtagReadDto;
import vistar.practice.demo.mapper.Mapper;
import vistar.practice.demo.repositories.HashtagRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HashtagService {
    private final HashtagRepository hashtagRepository;
    private final Mapper mapper;

    public List<HashtagReadDto> findAll() {
        return hashtagRepository.findAll()
                .stream().map(mapper::toHashtagReadDto)
                .toList();
    }

    public Optional<HashtagReadDto> findById(Long id){
        return  hashtagRepository.findById(id)
                .map(mapper::toHashtagReadDto);
    }

    @Transactional
    public HashtagReadDto create(HashtagCreateEditDto hashtagCreateEditDto) {
        return Optional.of(hashtagCreateEditDto)
                .map(mapper::toHashtagEntity)
                .map(hashtagRepository::save)
                .map(mapper::toHashtagReadDto)
                .orElseThrow();
    }

    @Transactional
    public Optional<HashtagReadDto> update(Long id ,HashtagCreateEditDto hashtagCreateEditDto) {
        return hashtagRepository.findById(id)
                .map((entity -> mapper.copyToEntityFromDto(entity, hashtagCreateEditDto)))
                .map(hashtagRepository::save)
                .map(mapper::toHashtagReadDto);
    }

    @Transactional
    public boolean delete (Long id) {
        return hashtagRepository.findById(id)
                .map(hashtagEntity -> {
                    hashtagRepository.delete(hashtagEntity);
                    return true;
                })
                .orElse(false);
    }
}

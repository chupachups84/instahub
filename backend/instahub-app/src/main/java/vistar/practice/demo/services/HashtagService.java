package vistar.practice.demo.services;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import vistar.practice.demo.dto.HashtagDto;
import vistar.practice.demo.mapper.Mapper;
import vistar.practice.demo.repositories.HashtagRepository;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HashtagService {
    HashtagRepository hashtagRepository;
    Mapper mapper;

    public List<HashtagDto> findAll() {
        return hashtagRepository.findAll()
                .stream().map(mapper::toHashtagDto)
                .toList();
    }

    public Optional<HashtagDto> findById(Long id){
        return  hashtagRepository.findById(id)
                .map(mapper::toHashtagDto);
    }

    @Transactional
    public HashtagDto create(HashtagDto hashtagDto) {
        return Optional.ofNullable(hashtagDto)
                .map(mapper::toHashtagEntity)
                .map(hashtagRepository::save)
                .map(mapper::toHashtagDto)
                .orElseThrow();
    }

    @Transactional
    public HashtagDto update(HashtagDto hashtagDto) {
        return Optional.ofNullable(hashtagDto)
                .map(mapper::toHashtagEntity)
                .map(hashtagRepository::save)
                .map(mapper::toHashtagDto)
                .orElseThrow();
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

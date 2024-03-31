package vistar.practice.demo.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vistar.practice.demo.dto.ReactionCreateEditDto;
import vistar.practice.demo.dto.ReactionReadDto;
import vistar.practice.demo.mappers.ReactionMapper;
import vistar.practice.demo.repositories.ReactionRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReactionService {
    private final ReactionRepository reactionRepository;
    ReactionMapper mapper;

    public List<ReactionReadDto> findAll() {
        return reactionRepository.findAll().stream()
                .map(mapper::toReadDto)
                .toList();
    }

    public Optional<ReactionReadDto> findById (Long id) {
        return reactionRepository.findById(id)
                .map(mapper::toReadDto);
    }

    @Transactional
    public ReactionReadDto create(ReactionCreateEditDto createEditDto) {
        return Optional.of(createEditDto)
                .map(mapper::toEntity)
                .map(reactionRepository::save)
                .map(mapper::toReadDto)
                .orElseThrow();
    }

    @Transactional
    public Optional<ReactionReadDto> update(Long id, ReactionCreateEditDto createEditDto) {
        return reactionRepository.findById(id)
                .map(entity -> mapper.copyToEntityFromDto(entity,createEditDto))
                .map(reactionRepository::save)
                .map(mapper::toReadDto);
    }

    @Transactional
    public boolean delete(Long id) {
        return reactionRepository.findById(id)
                .map((entity) -> {
                    reactionRepository.delete(entity);
                    return true;
                })
                .orElse(false);
    }
}

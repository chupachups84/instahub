package vistar.practice.demo.mapper;

import org.springframework.stereotype.Component;
import vistar.practice.demo.dto.ReactionCreateEditDto;
import vistar.practice.demo.dto.ReactionReadDto;
import vistar.practice.demo.models.ReactionEntity;



@Component
public class ReactionMapper {
    public ReactionReadDto toReadDto(ReactionEntity reactionEntity) {
        return new ReactionReadDto(
                reactionEntity.getId(),
                reactionEntity.getIconUrl(),
                reactionEntity.getName(),
                reactionEntity.getCreatedAt()
        );
    }
    public ReactionEntity toEntity (ReactionCreateEditDto createEditDto) {
        return ReactionEntity.builder()
                .iconUrl(createEditDto.getUrl())
                .name(createEditDto.getName())
                .createdAt(createEditDto.getCreatedAt())
                .build();
    }

    public ReactionEntity copyToEntityFromDto(ReactionEntity entity ,ReactionCreateEditDto createEditDto) {
        entity.setIconUrl(createEditDto.getUrl());
        entity.setName(createEditDto.getName());
        entity.setCreatedAt(createEditDto.getCreatedAt());
        return entity;
    }
}

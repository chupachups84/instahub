package vistar.practice.demo.mappers;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.springframework.stereotype.Component;
import vistar.practice.demo.dto.ReactionCreateEditDto;
import vistar.practice.demo.dto.ReactionReadDto;
import vistar.practice.demo.models.ReactionEntity;



@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface ReactionMapper {
    ReactionReadDto toReadDto (ReactionEntity reactionEntity);
    ReactionEntity toEntity (ReactionCreateEditDto createEditDto);
    default ReactionEntity copyToEntityFromDto(ReactionEntity entity ,ReactionCreateEditDto createEditDto) {
        entity.setIconUrl(createEditDto.getUrl());
        entity.setName(createEditDto.getName());
        entity.setCreatedAt(createEditDto.getCreatedAt());
        return entity;
    }
}

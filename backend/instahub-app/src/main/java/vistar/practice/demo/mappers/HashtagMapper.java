package vistar.practice.demo.mappers;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import vistar.practice.demo.dtos.hashtag.HashtagCreateEditDto;
import vistar.practice.demo.dtos.hashtag.HashtagReadDto;
import vistar.practice.demo.models.HashtagEntity;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface HashtagMapper {
    HashtagReadDto toReadDto(HashtagEntity hashtagEntity);

    HashtagEntity toEntity(HashtagCreateEditDto hashtagCreateEditDto);

    default HashtagEntity copyToEntityFromDto(HashtagEntity entity, HashtagCreateEditDto createEditDto) {
        entity.setText(createEditDto.getText());
        entity.setCreatedAt(createEditDto.getCreatedAt());
        return entity;
    }
}

package vistar.practice.demo.mapper;

import org.springframework.stereotype.Component;
import vistar.practice.demo.dto.HashtagCreateEditDto;
import vistar.practice.demo.dto.HashtagReadDto;
import vistar.practice.demo.models.HashtagEntity;

@Component
public class HashtagMapper {
    public HashtagReadDto toReadDto(HashtagEntity hashtagEntity) {
        return new HashtagReadDto(
                hashtagEntity.getId(),
                hashtagEntity.getText(),
                hashtagEntity.getCreatedAt()
        );
    }

     public HashtagEntity toEntity(HashtagCreateEditDto hashtagCreateEditDto) {
        return HashtagEntity.builder()
                .text(hashtagCreateEditDto.getText())
                .createdAt(hashtagCreateEditDto.getCreatedAt())
                .build();
     }

     public HashtagEntity copyToEntityFromDto (HashtagEntity entity, HashtagCreateEditDto createEditDto) {
        entity.setText(createEditDto.getText());
        entity.setCreatedAt(createEditDto.getCreatedAt());
        return entity;
     }
}

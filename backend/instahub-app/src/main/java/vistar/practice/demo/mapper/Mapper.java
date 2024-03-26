package vistar.practice.demo.mapper;

import vistar.practice.demo.dto.HashtagDto;
import vistar.practice.demo.models.HashtagEntity;

public class Mapper {
    public HashtagDto toHashtagDto (HashtagEntity hashtagEntity) {
        return new HashtagDto(
                hashtagEntity.getId(),
                hashtagEntity.getText(),
                hashtagEntity.getCreatedAt()
        );
    }
     public HashtagEntity toHashtagEntity (HashtagDto hashtagDto) {
         return new HashtagEntity(
                 hashtagDto.getId(),
                 hashtagDto.getText(),
                 hashtagDto.getCreatedAt()
         );
     }
}

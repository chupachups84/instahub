package vistar.practice.demo.mappers;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import vistar.practice.demo.dtos.reaction.ReactionCreateEditDto;
import vistar.practice.demo.dtos.reaction.ReactionReadDto;
import vistar.practice.demo.models.ReactionEntity;
import vistar.practice.demo.models.ReactionsPhotosEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface ReactionMapper {
    ReactionReadDto toReadDto (ReactionEntity reactionEntity);
    ReactionEntity toEntity (ReactionCreateEditDto createEditDto);

    default Map<String, Integer> toHashMap(List<ReactionsPhotosEntity> reactionsPhotosList) {
        Map<String, Integer> hashMap = new HashMap<>();
        for (var entity : reactionsPhotosList) {
            var reactionName = entity.getReaction().getName().name();
            if (!hashMap.containsKey(reactionName)) {
                hashMap.put(reactionName, 1);
            } else {
                hashMap.put(reactionName, hashMap.get(reactionName) + 1);
            }
        }
        return hashMap;
    }
}

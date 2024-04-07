package vistar.practice.demo.mappers;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import vistar.practice.demo.dtos.comment.CommentDto;
import vistar.practice.demo.models.CommentEntity;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface CommentMapper {

    CommentDto toDto(CommentEntity commentEntity);

    CommentEntity toEntity(CommentDto commentDto);

    default void updateFromDto(CommentDto commentDto, CommentEntity commentEntity) {
        if (commentDto.getText() != null) {
            commentEntity.setText(commentDto.getText());
        }
        if (commentDto.getIsShown() != null) {
            commentEntity.setIsShown(commentDto.getIsShown());
        }
        if (commentDto.getUpdatedAt() != null) {
            commentEntity.setUpdatedAt(commentDto.getUpdatedAt());
        }
    }
}

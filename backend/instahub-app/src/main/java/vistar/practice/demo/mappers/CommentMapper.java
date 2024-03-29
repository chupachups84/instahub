package vistar.practice.demo.mappers;

import vistar.practice.demo.dtos.comment.CommentDto;
import vistar.practice.demo.models.CommentEntity;

public class CommentMapper {

    public CommentMapper() {
        throw new RuntimeException("Utility class");
    }

    public static CommentDto toDto(CommentEntity commentEntity) {
        return CommentDto.builder()
                .id(commentEntity.getId())
                .text(commentEntity.getText())
                .isShown(commentEntity.getIsShown())
                .createdAt(commentEntity.getCreatedAt())
                .updatedAt(commentEntity.getUpdatedAt())
                .build();
    }

    public static CommentEntity toEntity(CommentDto commentDto) {
        return CommentEntity.builder()
                .text(commentDto.getText())
                .createdAt(commentDto.getCreatedAt())
                .updatedAt(commentDto.getUpdatedAt())
                .build();
    }

    public static void updateFromDto(CommentDto commentDto, CommentEntity commentEntity) {
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

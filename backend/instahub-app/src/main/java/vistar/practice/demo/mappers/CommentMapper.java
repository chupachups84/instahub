package vistar.practice.demo.mappers;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import vistar.practice.demo.dtos.comment.CommentDto;
import vistar.practice.demo.dtos.comment.CommentLoadDto;
import vistar.practice.demo.models.comment.CommentEntity;
import vistar.practice.demo.models.comment.CommentView;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface CommentMapper {

    @Mapping(target = "senderFullName", expression = "java(commentView.getUserFullName())")
    CommentDto toDto(CommentView commentView);

    CommentEntity toEntity(CommentLoadDto commentLoadDto);
}

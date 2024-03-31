package vistar.practice.demo.mappers;


import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import vistar.practice.demo.dtos.photo.PhotoDto;
import vistar.practice.demo.models.PhotoEntity;

import java.time.LocalDateTime;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface PhotoMapper {

    PhotoDto toDto(PhotoEntity photoEntity);

    @Mapping(target = "isAvatar", expression = "java(photoDto.getIsAvatar() != null && photoDto.getIsAvatar())")
    PhotoEntity toEntity(PhotoDto photoDto);

    default void updateFromDto(PhotoDto photoDto, PhotoEntity photoEntity) {

        if (photoDto.getStorageUrl() != null) {
            photoEntity.setStorageUrl(photoDto.getStorageUrl());
        }
        if (photoDto.getIconUrl() != null) {
            photoEntity.setIconUrl(photoDto.getIconUrl());
        }
        if (photoDto.getIsAvatar() != null) {
            photoEntity.setAvatar(photoDto.getIsAvatar());
        }
        if (photoDto.getIsShown() != null) {
            photoEntity.setShown(photoDto.getIsShown());
        }
    }
}

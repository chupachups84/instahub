package vistar.practice.demo.mappers;


import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import vistar.practice.demo.dtos.photo.PhotoInfoDto;
import vistar.practice.demo.models.PhotoEntity;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface PhotoMapper {

    PhotoInfoDto toDto(PhotoEntity photoEntity);

    @Mapping(target = "isAvatar", expression = "java(photoInfoDto.getIsAvatar() != null && photoInfoDto.getIsAvatar())")
    PhotoEntity toEntity(PhotoInfoDto photoInfoDto);

    default void updateFromDto(PhotoInfoDto photoInfoDto, PhotoEntity photoEntity) {

        if (photoInfoDto.getStorageUrl() != null) {
            photoEntity.setStorageUrl(photoInfoDto.getStorageUrl());
        }
        if (photoInfoDto.getIconUrl() != null) {
            photoEntity.setIconUrl(photoInfoDto.getIconUrl());
        }
        if (photoInfoDto.getIsAvatar() != null) {
            photoEntity.setAvatar(photoInfoDto.getIsAvatar());
        }
        if (photoInfoDto.getIsShown() != null) {
            photoEntity.setShown(photoInfoDto.getIsShown());
        }
    }
}

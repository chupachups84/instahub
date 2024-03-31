package vistar.practice.demo.mappers;


import vistar.practice.demo.dtos.photo.PhotoDto;
import vistar.practice.demo.models.PhotoEntity;

public class PhotoMapper {

    public PhotoMapper() { throw new RuntimeException("Utility class"); }

    public static PhotoDto toDto(PhotoEntity photoEntity) {

        return PhotoDto.builder()
                .id(photoEntity.getId())
                .storageUrl(photoEntity.getStorageUrl())
                .iconUrl(photoEntity.getStorageUrl())
                .createdAt(photoEntity.getCreatedAt())
                .isAvatar(photoEntity.isAvatar())
                .isShown(photoEntity.isShown())
                .build();
    }

    public static PhotoEntity toEntity(PhotoDto photoDto) {

        return PhotoEntity.builder()
                .storageUrl(photoDto.getStorageUrl())
                .iconUrl(photoDto.getIconUrl())
                .isAvatar(photoDto.getIsAvatar() != null && photoDto.getIsAvatar())
                .isShown(photoDto.getIsShown())
                .build();
    }

    public static void updateFromDto(PhotoDto photoDto, PhotoEntity photoEntity) {

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

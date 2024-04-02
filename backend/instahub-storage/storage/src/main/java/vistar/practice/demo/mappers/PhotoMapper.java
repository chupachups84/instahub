package vistar.practice.demo.mappers;


import vistar.practice.demo.dtos.photo.PhotoDto;
import vistar.practice.demo.dtos.photo.PhotoStorageDto;

public class PhotoMapper {

    public PhotoMapper() { throw new RuntimeException("Utility class"); }

    public static PhotoDto toInfoDto(
            PhotoStorageDto photoStorageDto,
            String storageUrl,
            String iconUrl
    ) {

        return PhotoDto.builder()
                .isAvatar(photoStorageDto.getIsAvatar())
                .ownerId(photoStorageDto.getOwnerId())
                .storageUrl(storageUrl)
                .iconUrl(iconUrl)
                .build();
    }
}

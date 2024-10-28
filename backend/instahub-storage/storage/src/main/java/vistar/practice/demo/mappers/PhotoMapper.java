package vistar.practice.demo.mappers;


import vistar.practice.demo.dtos.photo.PhotoInfoDto;
import vistar.practice.demo.dtos.photo.PhotoStorageDto;

import java.util.List;

public class PhotoMapper {

    public PhotoMapper() { throw new RuntimeException("Utility class"); }

    public static PhotoInfoDto toInfoDto(
            PhotoStorageDto photoStorageDto,
            String storageUrl,
            String iconUrl,
            String feedUrl
    ) {

        return PhotoInfoDto.builder()
                .isAvatar(photoStorageDto.getIsAvatar())
                .ownerId(photoStorageDto.getOwnerId())
                .description(photoStorageDto.getDescription())
                .hashtags(List.copyOf(photoStorageDto.getHashtags()))
                .storageUrl(storageUrl)
                .feedUrl(feedUrl)
                .iconUrl(iconUrl)
                .build();
    }
}

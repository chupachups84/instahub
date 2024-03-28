package vistar.practice.demo.mappers;

import lombok.extern.slf4j.Slf4j;
import vistar.practice.demo.dtos.photo.PhotoDto;
import vistar.practice.demo.dtos.photo.PhotoStorageDto;
import vistar.practice.demo.dtos.photo.PhotoUploadDto;

import java.io.IOException;
import java.util.Objects;

@Slf4j
public class PhotoUploadMapper {

    public PhotoUploadMapper() { throw new RuntimeException("Utility class"); }

    public static PhotoStorageDto toStorageDto(PhotoUploadDto photoUploadDto) {

        try {
            return PhotoStorageDto.builder()
                    .data(photoUploadDto.getFile().getBytes())
                    .ownerId(photoUploadDto.getOwnerId())
                    .suffix(
                            parseSuffix(Objects.requireNonNull(photoUploadDto.getFile().getOriginalFilename()))
                    )
                    .build();
        } catch (IOException ex) {
            log.error(
                    "Error while processing file " + photoUploadDto.getFile().getOriginalFilename(),
                    ex
            );
        }
        return null;
    }

    public static PhotoDto toEntityDto(PhotoUploadDto photoUploadDto) {

        return PhotoDto.builder()
                .isAvatar(photoUploadDto.getIsAvatar())
                .storageUrl(
                        photoUploadDto.getOwnerId().toString() + "/"
                                + photoUploadDto.getFile().getOriginalFilename()
                )
                .iconUrl(
                        photoUploadDto.getOwnerId().toString() + "/"
                                + photoUploadDto.getFile().getOriginalFilename()
                )
                .build();
    }

    private static String parseSuffix(String filename) {

        if (!filename.contains(".")) {
            return "";
        }

        int index = filename.length() - 1;
        while (filename.charAt(index) != '.') {
            --index;
        }

        return filename.substring(index);
    }
}

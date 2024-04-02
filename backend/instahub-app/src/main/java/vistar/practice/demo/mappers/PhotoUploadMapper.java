package vistar.practice.demo.mappers;


import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.springframework.web.multipart.MultipartFile;
import vistar.practice.demo.dtos.photo.PhotoDto;
import vistar.practice.demo.dtos.photo.PhotoStorageDto;
import vistar.practice.demo.dtos.photo.PhotoUploadDto;

import java.io.IOException;


@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface PhotoUploadMapper {

    @Mapping(target = "data", expression = "java(_getBytes(photoUploadDto.getFile()))")
    @Mapping(target = "photoId", expression = "java(photoId)")
    @Mapping(target = "suffix", expression = "java(_parseSuffix(java.util.Objects.requireNonNull(photoUploadDto.getFile().getOriginalFilename())))")
    PhotoStorageDto toStorageDto(PhotoUploadDto photoUploadDto, long photoId);

    PhotoDto toEntityDto(PhotoUploadDto photoUploadDto);

    default String _parseSuffix(String filename) {

        if (!filename.contains(".")) {
            return "";
        }

        int index = filename.length() - 1;
        while (filename.charAt(index) != '.') {
            --index;
        }

        return filename.substring(index);
    }

    default byte[] _getBytes (MultipartFile file) {
        try {
            return file.getBytes();
        }
        catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}

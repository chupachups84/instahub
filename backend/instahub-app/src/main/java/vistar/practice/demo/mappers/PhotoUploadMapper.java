package vistar.practice.demo.mappers;


import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import vistar.practice.demo.dtos.photo.PhotoDto;
import vistar.practice.demo.dtos.photo.PhotoStorageDto;
import vistar.practice.demo.dtos.photo.PhotoUploadDto;


@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface PhotoUploadMapper {
    //todo: add try catch and logs functionality with aop

    @Mapping(target = "data", expression = "java(photoUploadDto.getFile().getBytes())")
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
}

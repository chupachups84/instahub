package vistar.practice.demo.dtos.photo.icon;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import vistar.practice.demo.ImageProcessor;
import vistar.practice.demo.dtos.photo.PhotoStorageDto;

@Component
public class IconProcessor {

    @Value("${icon.width}")
    private int iconWidth;

    @Value("${icon.height}")
    private int iconHeight;

    @Value("${icon.format}")
    private String iconFormat;

    public PhotoStorageDto formIconDto(PhotoStorageDto originalPhotoStorageDto) {

        var bufferedPhoto = ImageProcessor.readFromBytes(originalPhotoStorageDto.getData());
        assert bufferedPhoto != null;
        var iconImage = ImageProcessor.resizeAndCrop(bufferedPhoto, iconWidth, iconHeight);
        var iconBytes = ImageProcessor.toBytes(iconImage, iconFormat);
        return new PhotoStorageDto(
                iconBytes,
                originalPhotoStorageDto.getOwnerId(),
                originalPhotoStorageDto.getPhotoId(),
                "." + iconFormat
        );
    }
}

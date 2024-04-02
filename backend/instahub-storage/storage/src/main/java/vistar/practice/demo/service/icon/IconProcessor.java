package vistar.practice.demo.service.icon;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vistar.practice.demo.ImageProcessor;
import vistar.practice.demo.dtos.photo.PhotoStorageDto;
import vistar.practice.demo.service.icon.impl.AvatarProcessor;
import vistar.practice.demo.service.icon.impl.UserPageIconProcessor;

@Component
@RequiredArgsConstructor
public class IconProcessor {

    private final AvatarProcessor avatarProcessor;
    private final UserPageIconProcessor userPageIconProcessor;

    public PhotoStorageDto formIconDto(PhotoStorageDto photoStorageDto, IconType iconType) {

        var iconParameters = switch (iconType) {
            case AVATAR -> avatarProcessor.getAvatarParameters();
            case USER_PAGE -> userPageIconProcessor.getUserPageIconParameters();
        };

        var originalImage = ImageProcessor.readFromBytes(photoStorageDto.getData());
        assert originalImage != null;

        var icon = ImageProcessor.resizeAndCrop(originalImage, iconParameters.iconWidth, iconParameters.iconHeight);
        var iconBytes = ImageProcessor.toBytes(icon, iconParameters.iconFormat);

        return new PhotoStorageDto(
                iconBytes,
                photoStorageDto.getOwnerId(),
                iconParameters.isAvatar,
                "." + iconParameters.iconFormat
        );
    }

    public enum IconType {
        USER_PAGE, AVATAR
    }

    @AllArgsConstructor
    public static class IconParameters {
        protected int iconWidth;
        protected int iconHeight;
        protected String iconFormat;
        protected boolean isAvatar;
    }
}

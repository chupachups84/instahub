package vistar.practice.demo.kafka.listeners.common;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import vistar.practice.demo.clients.app.AppClient;
import vistar.practice.demo.dtos.photo.PhotoStorageDto;
import vistar.practice.demo.service.icon.IconProcessor;
import vistar.practice.demo.service.StorageService;

import java.util.UUID;

import static vistar.practice.demo.mappers.PhotoMapper.toInfoDto;

@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaListeners {

    private final StorageService storageService;
    private final IconProcessor iconProcessor;
    private final AppClient appClient;

    @Value("${storage.bucket.photo}")
    private String photoBucket;

    @Value("${storage.bucket.icon}")
    private String iconBucket;

    @KafkaListener(
            topics = "${kafka.topic.photo}"
    )
    @Transactional
    public void handlePhoto(PhotoStorageDto photoStorageDto) {

        log.info("Handling photo (ownerId = " + photoStorageDto.getOwnerId() + ")");

        // Формируем DTO для иконок в ленте и сохраняем иконку
        var iconStorageDto = iconProcessor.formIconDto(photoStorageDto, IconProcessor.IconType.USER_PAGE);
        var iconUUID = handleFile(iconStorageDto, iconBucket);

        // Формируем DTO для аватарки, если фото отмечено как аватарка, и сохраняем аватарку
        if (photoStorageDto.getIsAvatar() != null && photoStorageDto.getIsAvatar()) {
            var avatarStorageDto = iconProcessor.formIconDto(photoStorageDto, IconProcessor.IconType.AVATAR);
            handleFile(avatarStorageDto, iconBucket);
        }

        // Сохраняем фото, переписав "на время" параметр для правильной процедуры сохранения
        var tmp = photoStorageDto.getIsAvatar();
        photoStorageDto.setIsAvatar(false);
        var photoUUID = handleFile(photoStorageDto, photoBucket);
        photoStorageDto.setIsAvatar(tmp);

        assert photoUUID != null && iconUUID != null;
        var photoDto = toInfoDto(
                photoStorageDto,
                photoBucket + "/" + photoStorageDto.getOwnerId() + "/"
                        + photoUUID + photoStorageDto.getSuffix(),
                iconBucket + "/" + iconStorageDto.getOwnerId() + "/"
                        + iconUUID + iconStorageDto.getSuffix()
        );
        appClient.sendPhotoInfo(photoDto);
    }

    private UUID handleFile(PhotoStorageDto photoStorageDto, String bucketName) {
        return storageService.saveIfNotExists(bucketName, photoStorageDto);
    }
}

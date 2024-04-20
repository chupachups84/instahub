package vistar.practice.demo.kafka.listeners.common;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import vistar.practice.demo.dtos.photo.PhotoStorageDto;
import vistar.practice.demo.kafka.producers.KafkaSender;
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
    private final KafkaSender kafkaSender;

    @Value("${storage.bucket.photo}")
    private String photoBucket;

    @Value("${storage.bucket.icon}")
    private String iconBucket;

    @Value("${icon.folder}")
    private String iconFolder;

    @Value("${feed.folder}")
    private String feedFolder;

    @Value("${kafka.topic.photo-info}")
    private String photoInfoTopic;

    @KafkaListener(
            topics = "${kafka.topic.photo}"
    )
    @Transactional
    public void handlePhoto(PhotoStorageDto photoStorageDto) {

        log.info("Handling photo (ownerId = " + photoStorageDto.getOwnerId() + ")");

        // Формируем DTO для иконок в профиле и сохраняем иконку
        var iconStorageDto = iconProcessor.formIconDto(photoStorageDto, IconProcessor.IconType.USER_PAGE);
        var iconUUID = handleFile(iconStorageDto, iconBucket, IconProcessor.IconType.USER_PAGE);

        // Формируем DTO для иконок в ленте и сохраняем иконку
        var feedStorageDto = iconProcessor.formIconDto(photoStorageDto, IconProcessor.IconType.FEED);
        var feedUUID = handleFile(feedStorageDto, iconBucket, IconProcessor.IconType.FEED);

        // Формируем DTO для аватарки, если фото отмечено как аватарка, и сохраняем аватарку
        if (photoStorageDto.getIsAvatar() != null && photoStorageDto.getIsAvatar()) {
            var avatarStorageDto = iconProcessor.formIconDto(photoStorageDto, IconProcessor.IconType.AVATAR);
            handleFile(avatarStorageDto, iconBucket, IconProcessor.IconType.AVATAR);
        }

        // Сохраняем фото, переписав "на время" параметр для правильной процедуры сохранения
        var tmp = photoStorageDto.getIsAvatar();
        photoStorageDto.setIsAvatar(false);
        var photoUUID = handleFile(photoStorageDto, photoBucket, null);
        photoStorageDto.setIsAvatar(tmp);


        assert photoUUID != null && iconUUID != null;
        var photoDto = toInfoDto(
                photoStorageDto,
                parseToStorageUrl(photoStorageDto.getOwnerId(), photoUUID, photoStorageDto.getSuffix()),
                parseToIconUrl(iconStorageDto.getOwnerId(), iconFolder, iconUUID, iconStorageDto.getSuffix()),
                parseToIconUrl(feedStorageDto.getOwnerId(), feedFolder, feedUUID, feedStorageDto.getSuffix())
        );

        kafkaSender.sendTransactionalMessage(photoInfoTopic, photoDto);
    }

    private UUID handleFile(PhotoStorageDto photoStorageDto, String bucketName, IconProcessor.IconType iconType) {
        return storageService.saveIfNotExists(bucketName, photoStorageDto, iconType);
    }

    private String parseToStorageUrl(long ownerId, UUID objectUUID, String suffix) {
        return photoBucket + "/" + ownerId + "/" + objectUUID + suffix;
    }

    private String parseToIconUrl(long ownerId, String folder, UUID objectUUID, String suffix) {
        return iconBucket + "/" + ownerId + "/" + folder + "/" + objectUUID + suffix;
    }
}

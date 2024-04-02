package vistar.practice.demo.kafka.listeners.common;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import vistar.practice.demo.clients.app.AppClient;
import vistar.practice.demo.dtos.photo.PhotoStorageDto;
import vistar.practice.demo.dtos.photo.icon.IconProcessor;
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
    public void handlePhoto(PhotoStorageDto originalPhotoStorageDto) {

        log.info("Handling photo (ownerId = " + originalPhotoStorageDto.getOwnerId() + ")");

        var iconStorageDto = iconProcessor.formIconDto(originalPhotoStorageDto);

        var photoUUID = handleFile(originalPhotoStorageDto, photoBucket);
        var iconUUID = handleFile(iconStorageDto, iconBucket);

        assert photoUUID != null && iconUUID != null;
        var photoDto = toInfoDto(
                originalPhotoStorageDto,
                photoBucket + "/" + originalPhotoStorageDto.getOwnerId() + "/"
                        + photoUUID + originalPhotoStorageDto.getSuffix(),
                iconBucket + "/" + iconStorageDto.getOwnerId() + "/"
                        + iconUUID + iconStorageDto.getSuffix()
        );
        appClient.sendPhotoInfo(photoDto);
    }

    private UUID handleFile(PhotoStorageDto photoStorageDto, String bucketName) {
        return storageService.saveIfNotExists(bucketName, photoStorageDto);
    }
}

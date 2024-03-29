package vistar.practice.demo.kafka.listeners.common;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import vistar.practice.demo.dtos.photo.PhotoStorageDto;
import vistar.practice.demo.service.StorageService;

@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaListeners {

    private final StorageService storageService;

    @Value("${storage.bucket.photo}")
    private String photoBucket;

    @KafkaListener(
            topics = "${kafka.topic.photo}"
    )
    @Transactional
    public void handlePhoto(PhotoStorageDto photoStorageDto) {
        log.info("Handling photo (id = " + photoStorageDto.getPhotoId() + ")");
        handleFile(photoStorageDto, photoBucket);
    }

    private void handleFile(PhotoStorageDto photoStorageDto, String bucketName) {
        storageService.saveIfNotExists(bucketName, photoStorageDto);
    }
}

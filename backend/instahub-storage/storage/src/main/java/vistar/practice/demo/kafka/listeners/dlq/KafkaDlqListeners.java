package vistar.practice.demo.kafka.listeners.dlq;

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
public class KafkaDlqListeners {

    private final StorageService storageService;

    @Value("${storage.bucket.dlq-prefix}${storage.bucket.photo}")
    private String dlqPhotoBucket;

    @KafkaListener(
            topics = "${kafka.topic.dlq-prefix}${kafka.topic.photo}"
    )
    @Transactional
    public void dlqPhotoHandler(PhotoStorageDto photoStorageDto) {

        log.warn("Photo of user (id = " + photoStorageDto.getOwnerId() + ") is up to be stashed in DLQ bucket");
        storageService.saveIfNotExists(dlqPhotoBucket, photoStorageDto, null);
    }
}

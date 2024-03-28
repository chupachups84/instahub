package vistar.practice.demo.kafka.listeners.common;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import vistar.practice.demo.dto.PhotoDto;
import vistar.practice.demo.service.StorageService;

@Component
@RequiredArgsConstructor
public class KafkaListeners {

    private final StorageService storageService;

    @Value("${storage.bucket.photo}")
    private String photoBucket;

    @KafkaListener(
            topics = "${kafka.topic.photo}"
    )
    @Transactional
    public void handlePhoto(PhotoDto photoDto) {
        handleFile(photoDto, photoBucket);
    }

    private void handleFile(PhotoDto photoDto, String bucketName) {
        storageService.saveIfNotExists(bucketName, photoDto);
    }
}

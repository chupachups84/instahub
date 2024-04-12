package vistar.practice.demo.kafka.listeners.common;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import vistar.practice.demo.dtos.photo.PhotoInfoDto;
import vistar.practice.demo.services.photo.PhotoService;

@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaPhotoListeners {

    private final PhotoService photoService;

    @KafkaListener(
            topics = "${kafka.topic.photo-info}"
    )
    @Transactional("kafkaTransactionManager")
    public void handlePhotoInfo(PhotoInfoDto photoInfoDto) {
        log.info("Handling photo info DTO (ownerId = {})", photoInfoDto.getOwnerId());
        photoService.save(photoInfoDto);
    }
}

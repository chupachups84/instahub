package vistar.practice.demo.kafka.listeners.dlq;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import vistar.practice.demo.dtos.photo.PhotoInfoDto;

@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaDlqListeners {

    @KafkaListener(
            topics = "${kafka.topic.dlq-prefix}${kafka.topic.photo-info}"
    )
    public void dlqPhotoHandler(PhotoInfoDto photoInfoDto) {
        log.error("Photo info DTO of user (id = {}) was not saved in DB", photoInfoDto.getOwnerId());
        if (photoInfoDto.getIsAvatar() != null && photoInfoDto.getIsAvatar()) {
            log.error("Avatar for user (id = {}) was rewritten in storage, but not saved in DB", photoInfoDto.getOwnerId());
        }
    }
}

package vistar.practice.demo.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vistar.practice.demo.dtos.photo.PhotoUploadDto;
import vistar.practice.demo.kafka.KafkaSender;
import vistar.practice.demo.mappers.PhotoUploadMapper;

@Service
@RequiredArgsConstructor
@Transactional("transactionManager")
@Slf4j
public class PhotoUploadService {

    @Value("${kafka.topic.photo}")
    private String photoTopic;
    private final PhotoUploadMapper photoUploadMapper;
    private final KafkaSender kafkaSender;

    public void store(PhotoUploadDto photoUploadDto) {

        final var photoStorageDto = photoUploadMapper.toStorageDto(photoUploadDto);
        kafkaSender.sendTransactionalMessage(photoTopic, photoStorageDto);
        log.info("Sent photo (ownerId = " + photoUploadDto.getOwnerId() + ") to kafka topic: " + photoTopic);
    }
}

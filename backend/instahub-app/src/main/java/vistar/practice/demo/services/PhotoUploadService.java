package vistar.practice.demo.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import vistar.practice.demo.dtos.photo.PhotoUploadDto;
import vistar.practice.demo.kafka.KafkaSender;
import vistar.practice.demo.mappers.PhotoUploadMapper;
import vistar.practice.demo.repositories.UserRepository;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Transactional("transactionManager")
@Slf4j
public class PhotoUploadService {

    @Value("${kafka.topic.photo}")
    private String photoTopic;
    private final PhotoUploadMapper photoUploadMapper;

    private final KafkaSender kafkaSender;
    private final UserRepository userRepository;

    public void store(PhotoUploadDto photoUploadDto) {

        final var photoStorageDto = photoUploadMapper.toStorageDto(photoUploadDto);
        kafkaSender.sendTransactionalMessage(photoTopic, photoStorageDto);
        log.info("Sent photo (ownerId = " + photoUploadDto.getOwnerId() + ") to kafka topic: " + photoTopic);
    }
}

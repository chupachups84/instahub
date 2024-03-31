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

@Service
@RequiredArgsConstructor
@Transactional("transactionManager")
@Slf4j
public class PhotoUploadService {

    @Value("${kafka.topic.photo}")
    private String photoTopic;

    @Value("${storage.service.url}")
    private String storageServiceUrl;

    private final KafkaSender kafkaSender;
    private final PhotoService photoService;
    private final PhotoUploadMapper photoUploadMapper;
    private final RestTemplate restTemplate;

    public void store(PhotoUploadDto photoUploadDto) {

        final var photoDto = photoUploadMapper.toEntityDto(photoUploadDto);
        final var photoEntity = photoService.save(photoDto);

        final var photoBucket = restTemplate.getForObject(
                storageServiceUrl + "/get/photo-bucket",
                String.class
        );
        photoEntity.setStorageUrl(
                photoBucket + "/" + photoEntity.getUser().getId() + "/" + photoEntity.getId()
        );

        final var photoStorageDto = photoUploadMapper.toStorageDto(photoUploadDto, photoEntity.getId());
        assert photoStorageDto != null;
        kafkaSender.sendTransactionalMessage(photoTopic, photoStorageDto);
        log.info("Sent photo (id = " + photoStorageDto.getPhotoId() + ") to kafka topic: " + photoTopic);
    }
}

package vistar.practice.demo.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vistar.practice.demo.dtos.photo.PhotoUploadDto;
import vistar.practice.demo.kafka.KafkaSender;
import vistar.practice.demo.mappers.PhotoUploadMapper;

@Service
@RequiredArgsConstructor
@Transactional
public class PhotoUploadService {

    @Value("${kafka.topic.photo}")
    private String photoTopic;

    private final KafkaSender kafkaSender;
    private final PhotoService photoService;

    public void store(PhotoUploadDto photoUploadDto) {

        var photoStorageDto = PhotoUploadMapper.toStorageDto(photoUploadDto);
        kafkaSender.sendTransactionalMessage(photoTopic, photoStorageDto);

        var photoDto = PhotoUploadMapper.toEntityDto(photoUploadDto);
        photoService.save(photoDto);
    }
}

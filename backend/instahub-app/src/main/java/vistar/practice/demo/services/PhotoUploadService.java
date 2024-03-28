package vistar.practice.demo.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import vistar.practice.demo.dtos.photo.PhotoUploadDto;
import vistar.practice.demo.kafka.KafkaSender;
import vistar.practice.demo.mappers.PhotoUploadMapper;

@Service
@RequiredArgsConstructor
public class PhotoUploadService {

    @Value("${kafka.topic.photo}")
    private String photoTopic;

    private final KafkaSender kafkaSender;

    public void store(PhotoUploadDto photoUploadDto) {
        var photoStorageDto = PhotoUploadMapper.toStorageDto(photoUploadDto);
        kafkaSender.sendTransactionalMessage(photoTopic, photoStorageDto);
    }
}

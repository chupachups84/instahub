package vistar.practice.demo.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vistar.practice.demo.aws.service.AwsService;
import vistar.practice.demo.dtos.photo.PhotoStorageDto;

import java.io.*;
import java.nio.file.Files;

@Service
@RequiredArgsConstructor
@Slf4j
public class StorageService {

    private final AwsService awsService;

    public void saveIfNotExists(String bucketName, PhotoStorageDto photoStorageDto) {

        var fileData = photoStorageDto.getData();

        try {

            File file = Files.createTempFile(
                    photoStorageDto.getOwnerId() + "-" + photoStorageDto.getPhotoId(),
                    photoStorageDto.getSuffix()
            ).toFile();

            try (OutputStream outputStream = new FileOutputStream(file)) {
                outputStream.write(fileData);
            }

            awsService.saveFile(
                    bucketName,
                    photoStorageDto.getOwnerId() + "/" + photoStorageDto.getPhotoId() + photoStorageDto.getSuffix(),
                    file
            );

        } catch (IOException ex) {
            log.error("Error while creating tempfile", ex);
        }
    }
}

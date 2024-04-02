package vistar.practice.demo.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vistar.practice.demo.aws.service.AwsService;
import vistar.practice.demo.dtos.photo.PhotoStorageDto;

import java.io.*;
import java.nio.file.Files;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class StorageService {

    private final AwsService awsService;

    /**
     * Сохраняет объект в хранилище и возвращает его UUID внутри него
     *
     * @param bucketName Название бакета
     * @param photoStorageDto DTO объекта
     * @return UUID объекта внутри хранилища
     */
    public UUID saveIfNotExists(String bucketName, PhotoStorageDto photoStorageDto) {

        var fileData = photoStorageDto.getData();

        try {

            UUID objectUUID = UUID.randomUUID();

            File file = Files.createTempFile(
                    photoStorageDto.getOwnerId() + "-" + objectUUID,
                    photoStorageDto.getSuffix()
            ).toFile();

            try (OutputStream outputStream = new FileOutputStream(file)) {
                outputStream.write(fileData);
            }

            awsService.saveFile(
                    bucketName,
                    photoStorageDto.getOwnerId() + "/" + objectUUID + photoStorageDto.getSuffix(),
                    file
            );

            return objectUUID;

        } catch (IOException ex) {
            log.error("Error while creating tempfile", ex);
        }
        return null;
    }
}

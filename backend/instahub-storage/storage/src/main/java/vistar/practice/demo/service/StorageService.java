package vistar.practice.demo.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${icon.folder}")
    private String iconFolder;

    @Value("${storage.bucket.icon}")
    private String iconBucket;

    @Value("${avatar.format}")
    private String avatarFormat;

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

            String key;
            if (photoStorageDto.getIsAvatar() != null && photoStorageDto.getIsAvatar()) {
                key = photoStorageDto.getOwnerId() + "/avatar" + photoStorageDto.getSuffix();
                awsService.deleteFile(bucketName, key);
            } else {
                key = bucketName.equals("icon") ?
                        photoStorageDto.getOwnerId() + "/" + iconFolder + "/" + objectUUID + photoStorageDto.getSuffix() :
                        photoStorageDto.getOwnerId() + "/" + objectUUID + photoStorageDto.getSuffix();
            }

            awsService.saveFile(bucketName, key, file);

            return objectUUID;

        } catch (IOException ex) {
            log.error("Error while creating tempfile", ex);
        }
        return null;
    }

    public byte[] browseObject(String objectUrl) {
        var urlParts = parseUrl(objectUrl);
        return awsService.getFileContent(urlParts[0], urlParts[1]);
    }

    public byte[] browseAvatar(long ownerId) {
        return awsService.getFileContent(iconBucket, ownerId + "/avatar." + avatarFormat);
    }

    private String[] parseUrl(String iconUrl) {
        String[] parts = new String[2];
        parts[0] = iconUrl.substring(0, iconUrl.indexOf("/"));
        parts[1] = iconUrl.substring(iconUrl.indexOf("/") + 1);
        return parts;
    }
}

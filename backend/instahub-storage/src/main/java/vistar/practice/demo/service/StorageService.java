package vistar.practice.demo.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vistar.practice.demo.aws.service.AwsService;
import vistar.practice.demo.dto.PhotoDto;

import java.io.*;
import java.nio.file.Files;

@Service
@RequiredArgsConstructor
@Slf4j
public class StorageService {

    private final AwsService awsService;

    public void saveIfNotExists(String bucketName, PhotoDto photoDto) {

        var fileData = photoDto.getData();

        try {

            File file = Files.createTempFile(
                    photoDto.getCreationDateTime().toString(),
                    photoDto.getSuffix()
            ).toFile();

            try (OutputStream outputStream = new FileOutputStream(file)) {
                outputStream.write(fileData);
            }

            awsService.saveFile(bucketName, file.getName(), file);

        } catch (IOException ex) {
            log.error("Error while creating tempfile", ex);
        }
    }
}

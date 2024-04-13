package vistar.practice.demo.aws.service;

import com.amazonaws.services.s3.AmazonS3;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

@Service
@RequiredArgsConstructor
@Slf4j
public class AwsService {

    private final AmazonS3 s3Client;

    public boolean fileExists(String bucketName, String key) {

        try {
            return s3Client.doesObjectExist(bucketName, key);
        } catch (Exception ex) {
            log.error("Exception while checking if file exists", ex);
        }
        return false;
    }

    public void saveFile(String bucketName, String key, File file) {
        if (!fileExists(bucketName, key)) {
            s3Client.putObject(bucketName, key, file);
        } else {
            log.warn("File already exists. New file (" + file.getName() + ") was ignored");
        }
    }

    public void deleteFile(String bucketName, String key) {
        if (fileExists(bucketName, key)) {
            s3Client.deleteObject(bucketName, key);
        } else {
            log.warn("File that is supposed to be deleted (" + bucketName + "/" + key + ") does not exist");
        }
    }

    public byte[] getFileContent(String bucketName, String key) {
        try {
            if (fileExists(bucketName, key)) {
                var s3ObjectInputStream = s3Client.getObject(bucketName, key).getObjectContent();

                var fileContent = new byte[s3ObjectInputStream.available()];
                s3ObjectInputStream.read(fileContent);

                return fileContent;
            }
        } catch (IOException ex) {
            log.error("Exception while reading file (bucket: {}, key: {}", bucketName, key, ex);
        }
        return null;
    }
}


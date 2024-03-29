package vistar.practice.demo.aws.service;

import com.amazonaws.services.s3.AmazonS3;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;

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

        try {
            if (!fileExists(bucketName, key)) {
                s3Client.putObject(bucketName, key, file);
            }
        } catch (Exception ex) {
            log.error("Exception while checking if file exists", ex);
        }
    }
}


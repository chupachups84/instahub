package vistar.practice.demo.aws.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class S3Configuration {

    @Value("${storage.url}")
    private String storageUrl;

    @Value("${storage.user}")
    private String storageUser;

    @Value("${storage.password}")
    private String storagePassword;

    @Value("${storage.region}")
    private String storageRegion;

    @Bean
    public AmazonS3 amazonS3() {

        return AmazonS3ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(
                        new BasicAWSCredentials(storageUser, storagePassword)))
                .withEndpointConfiguration(new AwsClientBuilder
                        .EndpointConfiguration(storageUrl, storageRegion))
                .build();
    }
}

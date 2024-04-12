package vistar.practice.demo.clients;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class StorageClient {

    @Value("${storage.url}")
    private String storageUrl;

    private final RestTemplate restTemplate;

    public byte[] requestObject(String objectUrl) {
        return restTemplate.getForObject(
                storageUrl + "/get/object-by-url?objectUrl=" + objectUrl,
                byte[].class
        );
    }
}

package vistar.practice.demo.clients.app;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import vistar.practice.demo.dtos.photo.PhotoDto;

import java.net.URL;

@Component
@RequiredArgsConstructor
public class AppClient {

    @Value("${app.url}")
    private URL appUrl;

    private final RestTemplate restTemplate;

    public void sendPhotoInfo(PhotoDto photoDto) {
        restTemplate.postForObject(appUrl + "/photos", photoDto, String.class);
    }
}

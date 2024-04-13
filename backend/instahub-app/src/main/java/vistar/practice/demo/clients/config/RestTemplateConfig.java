package vistar.practice.demo.clients.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate() {

        final var restTemplate = new RestTemplateBuilder().build();

        final var rf = new SimpleClientHttpRequestFactory();
        rf.setReadTimeout(300_000);
        rf.setConnectTimeout(300_000);

        restTemplate.setRequestFactory(rf);

        return restTemplate;
    }
}

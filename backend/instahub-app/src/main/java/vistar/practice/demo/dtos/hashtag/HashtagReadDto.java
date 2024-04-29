package vistar.practice.demo.dtos.hashtag;

import lombok.Value;

import java.time.Instant;

@Value
public class HashtagReadDto {
    Long id;

    String text;

    Instant createdAt;
}

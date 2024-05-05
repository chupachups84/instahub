package vistar.practice.demo.dtos.hashtag;

import lombok.Value;

import java.time.Instant;

@Value
public class HashtagCreateEditDto {
    String text;
    Long photoId;
    Instant createdAt;
}

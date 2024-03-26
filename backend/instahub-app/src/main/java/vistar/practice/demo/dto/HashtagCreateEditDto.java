package vistar.practice.demo.dto;

import lombok.Value;

import java.time.Instant;

@Value
public class HashtagCreateEditDto {
    String text;
    Instant createdAt;
}

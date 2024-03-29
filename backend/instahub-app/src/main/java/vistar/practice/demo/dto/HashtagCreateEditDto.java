package vistar.practice.demo.dto;

import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.Value;

import java.time.Instant;

@Value
public class HashtagCreateEditDto {
    @Size(min = 2, max = 64)
    String text;

    @PastOrPresent
    Instant createdAt;
}

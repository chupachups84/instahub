package vistar.practice.demo.dto;

import jakarta.validation.constraints.PastOrPresent;
import lombok.Value;
import vistar.practice.demo.models.ReactionName;

import java.net.URL;
import java.time.Instant;

@Value
public class ReactionCreateEditDto {
    URL url;
    ReactionName name;

    @PastOrPresent
    Instant createdAt;
}

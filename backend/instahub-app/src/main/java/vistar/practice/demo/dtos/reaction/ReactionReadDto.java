package vistar.practice.demo.dtos.reaction;

import jakarta.validation.constraints.PastOrPresent;
import lombok.Value;
import vistar.practice.demo.models.ReactionName;

import java.time.Instant;

@Value
public class ReactionReadDto {
    Long id;
    ReactionName name;
    @PastOrPresent
    Instant createdAt;
}

package vistar.practice.demo.dtos.reaction;

import lombok.Value;
import vistar.practice.demo.models.ReactionName;

@Value
public class ReactionCreateEditDto {
    ReactionName name;
}

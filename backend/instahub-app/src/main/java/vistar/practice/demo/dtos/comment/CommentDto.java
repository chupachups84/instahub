package vistar.practice.demo.dtos.comment;

import lombok.Data;
import lombok.Builder;

import java.time.Instant;

@Data
@Builder
public class CommentDto {
    private Long id;
    private String text;
    private Boolean isShown;
    private Instant createdAt;
    private Instant updatedAt;
}

package vistar.practice.demo.dtos.comment;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Builder;

import java.time.Instant;

@Data
@Builder
public class CommentDto {
    private Long id;

    @NotEmpty
    @Size(max = 200)
    private String text;

    private Boolean isShown;

    @PastOrPresent
    private Instant createdAt;

    private Instant updatedAt;
}

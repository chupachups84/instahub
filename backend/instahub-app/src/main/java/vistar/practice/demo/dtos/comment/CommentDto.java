package vistar.practice.demo.dtos.comment;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Builder;

import java.time.LocalDateTime;

@Data
@Builder
public class CommentDto {

    @NotEmpty
    @Size(max = 1024)
    private String text;

    private Boolean isShown;

    @PastOrPresent
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private String senderFullName;
}

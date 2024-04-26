package vistar.practice.demo.dtos.comment;

import lombok.Data;
import lombok.Builder;

import java.time.LocalDateTime;

@Data
@Builder
public class CommentDto {

    private String text;

    private Boolean isShown;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private String senderFullName;
}

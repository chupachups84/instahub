package vistar.practice.demo.dtos.comment;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Builder;

@Data
@Builder
public class CommentLoadDto {

    @NotEmpty
    @Size(max = 1024)
    private String text;

    private long photoId;
}

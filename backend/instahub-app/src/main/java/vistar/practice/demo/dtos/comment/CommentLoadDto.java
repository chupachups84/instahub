package vistar.practice.demo.dtos.comment;

import lombok.Data;
import lombok.Builder;

@Data
@Builder
public class CommentLoadDto {

    private String text;

    private long photoId;
}

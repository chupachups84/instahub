package vistar.practice.demo.dtos.photo.load;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.core.io.InputStreamSource;
import vistar.practice.demo.dtos.comment.CommentDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FeedPhotoDto {

    InputStreamSource photoInputStream;

    CommentDto lastComment;

    String ownerFullName;

    LocalDateTime createdAt;

    Map<String, Integer> reactions;

    Integer repostCount;

    String description;

    List<String> hashtags;
}

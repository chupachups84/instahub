package vistar.practice.demo.dtos.photo.load;

import lombok.AllArgsConstructor;
import org.springframework.core.io.InputStreamSource;
import vistar.practice.demo.dtos.comment.CommentDto;

import java.time.LocalDateTime;

@AllArgsConstructor
public class FeedPhotoDto {

    InputStreamSource photoInputStream;

    //todo -> добавить ReactionDto, HashtagDto и RepostDto по готовности
    CommentDto lastComment;

    String ownerFullName;

    LocalDateTime createdAt;
}

package vistar.practice.demo.controllers.comment;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vistar.practice.demo.dtos.comment.CommentDto;
import vistar.practice.demo.services.comment.CommentLoadService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "${comment-load.uri}")
public class CommentLoadController {

    private final CommentLoadService commentLoadService;

    @GetMapping
    public ResponseEntity<CommentDto> handleCommentByOffset(
            @RequestParam LocalDateTime lastCommentCreationTime,
            @RequestParam long photoId,
            @RequestParam long commentOffset
    ) {
        return ResponseEntity.ok(commentLoadService.getByOffset(lastCommentCreationTime, photoId, commentOffset));
    }

    @GetMapping("/comments")
    public ResponseEntity<List<CommentDto>> handleComments(
            @RequestParam LocalDateTime lastCommentCreationTime,
            @RequestParam long photoId,
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "20") int size
    ) {
        return ResponseEntity.ok(commentLoadService.fetchLoad(lastCommentCreationTime, photoId, page, size));
    }
}

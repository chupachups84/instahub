package vistar.practice.demo.controllers.comment;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import vistar.practice.demo.dtos.comment.CommentLoadDto;
import vistar.practice.demo.services.comment.CommentService;

import java.security.Principal;
import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "${comment.uri}")
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<String> addComment(
            @RequestBody @Validated CommentLoadDto commentLoadDto,
            Principal principal
    ) {
        commentService.save(commentLoadDto, principal.getName());
        return ResponseEntity.ok("Comment has been successfully created");
    }

    @PutMapping
    public ResponseEntity<String> update(
            @RequestParam LocalDateTime lastCommentCreationTime,
            @RequestBody CommentLoadDto commentLoadDto,
            @RequestParam long commentOffset,
            Principal principal
    ) {
        commentService.update(lastCommentCreationTime, commentLoadDto, commentOffset, principal.getName());
        return ResponseEntity.ok("Comment has been updated");
    }

    @DeleteMapping
    public ResponseEntity<String> deleteComment(
            @RequestParam LocalDateTime lastCommentCreationTime,
            @RequestBody long photoId,
            @RequestParam long commentOffset,
            Principal principal
    ) {
        commentService.delete(lastCommentCreationTime, photoId, commentOffset, principal.getName());
        return ResponseEntity.ok("Comment has been deleted");
    }

}

package vistar.practice.demo.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import vistar.practice.demo.dtos.comment.CommentDto;
import vistar.practice.demo.services.CommentService;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "${comment.uri}")
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<String> addComment(@RequestBody @Validated CommentDto commentDto) {
        commentService.save(commentDto);
        return ResponseEntity.ok("Comment has been successfully created");
    }

    @GetMapping("/{commentId}")
    public ResponseEntity<CommentDto> getComment(@PathVariable long commentId) {
        return ResponseEntity.ok(commentService.findById(commentId));
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<String> updateComment(
            @PathVariable long commentId,
            @RequestBody @Validated CommentDto commentDto
    ) {
        commentService.update(commentId, commentDto);
        return ResponseEntity.ok("Comment has been updated");
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable long commentId) {
        commentService.delete(commentId);
        return ResponseEntity.ok("Comment has been deleted");
    }

}

package vistar.practice.demo.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vistar.practice.demo.dtos.reaction.ReactionCreateEditDto;
import vistar.practice.demo.dtos.reaction.ReactionReadDto;
import vistar.practice.demo.services.ReactionPhotoService;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${reactions.uri}")
public class ReactionPhotoController {
    private final ReactionPhotoService reactionPhotoService;

    @GetMapping()
    public ResponseEntity<List<ReactionReadDto>> getAllReactions(@PathVariable Long photoId) {
        return ResponseEntity.ok().body(reactionPhotoService.getAllReactionsByPhotoId(photoId));
    }

    @PostMapping()
    public ResponseEntity<ReactionReadDto> reaction(
            @PathVariable Long photoId,
            @RequestBody ReactionCreateEditDto reactionCreateEditDto,
            Principal principal
    ) {
        return ResponseEntity.ok().body(reactionPhotoService.reactOnPhoto(photoId, reactionCreateEditDto, principal.getName()));
    }

    @DeleteMapping()
    public void deleteReaction(
            @PathVariable Long photoId,
            @RequestBody ReactionCreateEditDto reactionCreateEditDto,
            Principal principal
    ) {
        reactionPhotoService.removeReactionFromPhoto(photoId, reactionCreateEditDto, principal.getName());
    }
}

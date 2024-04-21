package vistar.practice.demo.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vistar.practice.demo.dtos.reaction.ReactionCreateEditDto;
import vistar.practice.demo.dtos.reaction.ReactionReadDto;
import vistar.practice.demo.services.ReactionService;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${reactions.uri}")
public class ReactionController {
    private final ReactionService reactionService;

    @GetMapping()
    public ResponseEntity<List<ReactionReadDto>> getAllReactions(@PathVariable Long photoId) {
        return ResponseEntity.ok().body(reactionService.getAllReactionsByPhotoId(photoId));
    }

    @PostMapping()
    public ResponseEntity<ReactionReadDto> reaction(
            @PathVariable Long photoId,
            @RequestBody ReactionCreateEditDto reactionCreateEditDto,
            Principal principal
    ) {
        return ResponseEntity.ok().body(reactionService.reactOnPhoto(photoId, reactionCreateEditDto, principal.getName()));
    }

    @DeleteMapping()
    public void deleteReaction(
            @PathVariable Long photoId,
            @RequestBody ReactionCreateEditDto reactionCreateEditDto,
            Principal principal
    ) {
        reactionService.removeReactionFromPhoto(photoId, reactionCreateEditDto, principal.getName());
    }
}

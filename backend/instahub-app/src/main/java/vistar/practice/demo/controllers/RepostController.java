package vistar.practice.demo.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vistar.practice.demo.dtos.repost.RepostDto;
import vistar.practice.demo.services.RepostService;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("${reposts.uri}")
public class RepostController {

    private final RepostService repostService;

    @PostMapping
    public ResponseEntity<String> reaction(
            @PathVariable Long photoId,
            @RequestBody RepostDto repostCreateEditDto,
            Principal principal
    ) {
        repostService.repostPhoto(photoId, repostCreateEditDto, principal.getName());
        return ResponseEntity.ok("Repost was successfully put into database");
    }
}

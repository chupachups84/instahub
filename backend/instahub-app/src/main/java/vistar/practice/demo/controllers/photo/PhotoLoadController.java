package vistar.practice.demo.controllers.photo;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamSource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vistar.practice.demo.dtos.photo.load.FeedPhotoDto;
import vistar.practice.demo.services.photo.PhotoLoadService;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "${photo-load.uri}")
public class PhotoLoadController {

    private final PhotoLoadService photoLoadService;

    @GetMapping("/icons")
    public ResponseEntity<List<InputStreamSource>> handleIconsLoad(
            @RequestParam(defaultValue = "0", required = false) Integer page,
            @RequestParam(defaultValue = "20", required = false) Integer size,
            @RequestParam String username
    ) {
        return ResponseEntity.ok(photoLoadService.fetchLoad(username, page, size));
    }

    @GetMapping("/photo/profile")
    public ResponseEntity<FeedPhotoDto> handlePhotoFromProfileLoad(
            @RequestParam int creationOffset,
            @RequestParam String username
    ) {
        return ResponseEntity.ok(photoLoadService.loadPhotoByIconCreationOffset(username, creationOffset));
    }

    @GetMapping("/photo/feed")
    public ResponseEntity<InputStreamSource> handlePhotoFromFeedLoad(
            @RequestParam LocalDateTime firstPhotoCreationTime,
            @RequestParam int creationOffset,
            Principal principal
    ) {
        return ResponseEntity.ok(
                photoLoadService.loadPhotoByFeedCreationOffset(
                        firstPhotoCreationTime,
                        principal.getName(),
                        creationOffset
                )
        );
    }

    @GetMapping("/avatar")
    public ResponseEntity<FeedPhotoDto> handleAvatarLoad(
            @RequestParam String username
    ) {
        return ResponseEntity.ok(photoLoadService.loadAvatar(username));
    }

    @GetMapping("/feed")
    public ResponseEntity<List<FeedPhotoDto>> handleFeedLoad(
            @RequestParam LocalDateTime firstPhotoCreationTime,
            @RequestParam(defaultValue = "0", required = false) Integer page,
            @RequestParam(defaultValue = "20", required = false) Integer size,
            Principal principal
    ) {
        return ResponseEntity.ok(
                photoLoadService.fetchFeed(
                        firstPhotoCreationTime,
                        principal.getName(),
                        page,
                        size
                )
        );
    }
}

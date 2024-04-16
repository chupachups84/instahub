package vistar.practice.demo.controllers.photo;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamSource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vistar.practice.demo.services.photo.PhotoLoadService;

import java.security.Principal;
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
            Principal principal
    ) {
        return ResponseEntity.ok(photoLoadService.fetchLoad(principal.getName(), page, size));
    }

    @GetMapping("/photo")
    public ResponseEntity<InputStreamSource> handlePhotoLoad(
            @RequestParam int creationOffset,
            Principal principal
    ) {
        return ResponseEntity.ok(photoLoadService.loadPhotoByCreationOffset(principal.getName(), creationOffset));
    }

    @GetMapping("/avatar")
    public ResponseEntity<InputStreamSource> handleAvatarLoad(
            Principal principal
    ) {
        return ResponseEntity.ok(photoLoadService.loadAvatar(principal.getName()));
    }
}

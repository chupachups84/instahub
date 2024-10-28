package vistar.practice.demo.controllers.photo;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import vistar.practice.demo.dtos.photo.PhotoInfoDto;
import vistar.practice.demo.services.photo.PhotoService;

import java.security.Principal;
import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "${photo.uri}")
public class PhotoController {

    private final PhotoService photoService;

    @PostMapping
    public ResponseEntity<String> addPhoto(@RequestBody @Validated PhotoInfoDto photoInfoDto) {
        photoService.save(photoInfoDto);
        return ResponseEntity.ok("Photo has been successfully created");
    }

    @GetMapping("/{photoId}")
    public ResponseEntity<PhotoInfoDto> getPhoto(@PathVariable long photoId) {
        return ResponseEntity.ok(photoService.findById(photoId));
    }

    @GetMapping("/photo-id/profile")
    public ResponseEntity<Long> getPhotoIdByIconCreationOffset(
            @RequestParam int creationOffset,
            Principal principal
    ) {
        return ResponseEntity.ok(
                photoService.getPhotoIdByIconCreationOffset(
                        principal.getName(),
                        creationOffset
                )
        );
    }

    @GetMapping("/photo-id/feed")
    public ResponseEntity<Long> getPhotoIdByFeedCreationOffset(
            @RequestParam LocalDateTime firstPhotoCreationTime,
            @RequestParam int creationOffset,
            Principal principal
    ) {
        return ResponseEntity.ok(
                photoService.getPhotoIdByFeedCreationOffset(
                        firstPhotoCreationTime,
                        principal.getName(),
                        creationOffset
                )
        );
    }

    @PutMapping("/{photoId}")
    public ResponseEntity<String> updatePhoto(
            @PathVariable long photoId,
            @RequestBody PhotoInfoDto photoInfoDto
    ) {
        photoService.update(photoId, photoInfoDto);
        return ResponseEntity.ok("Photo has been updated");
    }

    @DeleteMapping("/{photoId}")
    public ResponseEntity<String> deletePhoto(@PathVariable long photoId) {
        photoService.delete(photoId);
        return ResponseEntity.ok("Photo has been deleted");
    }
}

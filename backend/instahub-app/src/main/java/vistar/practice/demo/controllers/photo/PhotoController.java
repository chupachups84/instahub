package vistar.practice.demo.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import vistar.practice.demo.dtos.photo.PhotoInfoDto;
import vistar.practice.demo.services.PhotoService;

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

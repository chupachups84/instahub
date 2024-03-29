package vistar.practice.demo.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vistar.practice.demo.dtos.photo.PhotoUploadDto;
import vistar.practice.demo.services.PhotoUploadService;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "${photo-upload.uri}")
public class PhotoUploadController {

    private final PhotoUploadService photoUploadService;

    @PostMapping
    public ResponseEntity<String> handleFileUpload(@RequestBody PhotoUploadDto photoUploadDto) {

        photoUploadService.store(photoUploadDto);
        return ResponseEntity.ok("File was successfully created");
    }
}

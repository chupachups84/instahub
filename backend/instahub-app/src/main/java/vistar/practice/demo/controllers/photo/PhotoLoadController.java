package vistar.practice.demo.controllers.photo;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamSource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vistar.practice.demo.dtos.photo.PhotoUploadDto;
import vistar.practice.demo.services.photo.PhotoLoadService;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "${photo-load.uri}")
public class PhotoLoadController {

    private final PhotoLoadService photoLoadService;

    @GetMapping
    public ResponseEntity<List<InputStreamSource>> handleFilesLoad(
            @RequestParam(defaultValue = "0", required = false) Integer page,
            @RequestParam(defaultValue = "20", required = false) Integer size,
            Principal principal
    ) {
        return ResponseEntity.ok(photoLoadService.fetchLoad(principal.getName(), page, size));
    }
}

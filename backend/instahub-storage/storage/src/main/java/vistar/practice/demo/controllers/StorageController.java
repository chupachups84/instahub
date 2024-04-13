package vistar.practice.demo.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vistar.practice.demo.service.StorageService;

@RestController
@RequestMapping(path = "${storage.controller.url}")
@RequiredArgsConstructor
public class StorageController {

    @Value("${storage.bucket.photo}")
    private String photoBucket;

    @Value("${storage.bucket.icon}")
    private String iconBucket;

    private final StorageService storageService;

    @GetMapping("/get/photo-bucket")
    public ResponseEntity<String> photoBucket() {
        return ResponseEntity.ok(photoBucket);
    }

    @GetMapping("/get/icon-bucket")
    public ResponseEntity<String> iconBucket() {
        return ResponseEntity.ok(iconBucket);
    }

    @GetMapping("/get/object-by-url")
    public ResponseEntity<byte[]> storageObject(@RequestParam String objectUrl) {
        return ResponseEntity.ok(storageService.browse(objectUrl));
    }
}

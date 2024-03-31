package vistar.practice.demo.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "${storage.controller.url}")
public class StorageController {

    @Value("${storage.bucket.photo}")
    private String photoBucket;

    @Value("${storage.bucket.icon}")
    private String iconBucket;

    @GetMapping("/get/photo-bucket")
    public ResponseEntity<String> photoBucket() {
        return ResponseEntity.ok(photoBucket);
    }

    @GetMapping("/get/icon-bucket")
    public ResponseEntity<String> iconBucket() {
        return ResponseEntity.ok(iconBucket);
    }
}

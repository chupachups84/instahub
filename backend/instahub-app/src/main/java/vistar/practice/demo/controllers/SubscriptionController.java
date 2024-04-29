package vistar.practice.demo.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vistar.practice.demo.dtos.user.UserResponseDto;
import vistar.practice.demo.services.SubscriptionService;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "${subscriptions.uri}")
public class SubscriptionController {
    private final SubscriptionService subscriptionService;

    @GetMapping()
    public ResponseEntity<List<UserResponseDto>> getSubscribers(@PathVariable Long id) {
        return ResponseEntity.ok(subscriptionService.getSubscribers(id));
    }

    @PostMapping("/{subId}")
    public ResponseEntity<String> subscribe(
            @PathVariable Long id,
            @PathVariable Long subId,
            Principal principal
    ) {
        subscriptionService.subscribe(id, subId, principal.getName());
        return ResponseEntity.ok("you successfully subscribe to user");
    }

    @DeleteMapping("/{subId}")
    public ResponseEntity<String> unsubscribe(
            @PathVariable Long id,
            @PathVariable Long subId,
            Principal principal
    ) {
        subscriptionService.unsubscribe(id, subId, principal.getName());
        return ResponseEntity.ok("you successfully unsubscribe to user");
    }


}

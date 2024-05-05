package vistar.practice.demo.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vistar.practice.demo.dtos.count.CountDto;
import vistar.practice.demo.dtos.user.UserResponseDto;
import vistar.practice.demo.services.SubscriptionService;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "${user.uri}")
@CrossOrigin
public class SubscriptionController {
    private final SubscriptionService subscriptionService;

    @GetMapping("/{username}/followers")
    public ResponseEntity<List<UserResponseDto>> getFollowers(
            @PathVariable String username,
            @RequestParam(required = false,defaultValue = "0") Integer page,
            @RequestParam(required = false,defaultValue = "10") Integer size
    ) {
        return ResponseEntity.ok(
                subscriptionService.getFollowers(username, PageRequest.of(page,size))
        );
    }

    @GetMapping("/{username}/followers/count")
    public ResponseEntity<CountDto> getFollowersCount(
            @PathVariable String username
    ) {
        return ResponseEntity.ok(subscriptionService.getFollowersCount(username));
    }

    @GetMapping("/{username}/follows/count")
    public ResponseEntity<CountDto> getFollowsCount(
            @PathVariable String username
    ) {
        return ResponseEntity.ok(subscriptionService.getFollowsCount(username));
    }

    @GetMapping("/{username}/follows")
    public ResponseEntity<List<UserResponseDto>> getFollows(
            @PathVariable String username,
            @RequestParam(required = false,defaultValue = "0") Integer page,
            @RequestParam(required = false,defaultValue = "10") Integer size
    ) {
        return ResponseEntity.ok(
                subscriptionService.getFollows(username, PageRequest.of(page,size))
        );
    }

    @GetMapping("/{usernameToCheck}/relation")
    public ResponseEntity<String> isSubscribed(
            @PathVariable String usernameToCheck,
            @RequestParam String username
    ) {
        return ResponseEntity.ok(subscriptionService.relation(username, usernameToCheck));
    }

    @PostMapping("/{username}/follow/{usernameToFollow}")
    public ResponseEntity<String> subscribe(
            @PathVariable String usernameToFollow,
            @PathVariable String username
    ) {
        subscriptionService.subscribe(usernameToFollow, username);
        return ResponseEntity.ok("you successfully subscribe to user");
    }

    @PostMapping("/{username}/unfollow/{usernameToUnfollow}")
    public ResponseEntity<String> unsubscribe(
            @PathVariable String usernameToUnfollow,
            @PathVariable String username
    ) {
        subscriptionService.unsubscribe(usernameToUnfollow, username);
        return ResponseEntity.ok("you successfully unsubscribe to user");
    }


}

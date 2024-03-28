package vistar.practice.demo.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vistar.practice.demo.dtos.token.TokenDto;
import vistar.practice.demo.dtos.user.UserResponseDto;
import vistar.practice.demo.services.UserService;

import java.security.Principal;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("${user.uri}")
public class UserController {
    private final UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUser(@PathVariable Long id) {
        return ResponseEntity.ok(userService.findById(id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<String> updateUser(
            Principal principal,
            @PathVariable Long id,
            @RequestParam(required = false) Optional<String> username,
            @RequestParam(required = false, name = "first_name") Optional<String> firstName,
            @RequestParam(required = false, name = "middle_name") Optional<String> middleName,
            @RequestParam(required = false, name = "last_name") Optional<String> lastName,
            @RequestParam(required = false) Optional<String> patronymic,
            @RequestParam(required = false) Optional<String> email,
            @RequestParam(required = false) Optional<String> password
    ) {
        userService.updateUser(principal.getName(),id, username, firstName, middleName, lastName, patronymic, email, password);
        return ResponseEntity.ok("user has been updated");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<TokenDto> deleteUser(@PathVariable Long id, Principal principal) {
        return ResponseEntity.ok(userService.deleteUser(id, principal.getName()));
    }

}


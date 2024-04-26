package vistar.practice.demo.controllers;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vistar.practice.demo.dtos.authentication.RegisterDto;
import vistar.practice.demo.dtos.token.TokenDto;
import vistar.practice.demo.dtos.user.PasswordDto;
import vistar.practice.demo.dtos.user.UserResponseDto;
import vistar.practice.demo.services.UserService;

import java.security.Principal;

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
            @RequestBody RegisterDto updateDtoUser,
            HttpServletResponse response
    ) {
        userService.updateUser(
                principal.getName(),id, updateDtoUser.getUsername(),
                updateDtoUser.getFirstName(), updateDtoUser.getMiddleName(), updateDtoUser.getLastName(),
                updateDtoUser.getPatronymic(), updateDtoUser.getEmail(),response);
        return ResponseEntity.ok("user has been updated");
    }

    @PatchMapping("/{id}/password")
    public ResponseEntity<TokenDto> changePassword(
            @PathVariable Long id,
            @RequestBody PasswordDto passwordDto,
            Principal principal,
            HttpServletResponse response
    ){
        return ResponseEntity.ok(userService.changePassword(id, passwordDto, principal.getName(),response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(
            @PathVariable Long id,
            Principal principal,
            HttpServletResponse response
    ) {
        userService.deleteUser(id, principal.getName(),response);
        return ResponseEntity.ok("user has been deleted");
    }
}


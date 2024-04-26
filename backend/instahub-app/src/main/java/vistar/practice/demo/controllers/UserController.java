package vistar.practice.demo.controllers;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import vistar.practice.demo.dtos.token.TokenDto;
import vistar.practice.demo.dtos.user.PasswordDto;
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
            HttpServletResponse response
    ) {
        userService.updateUser(principal.getName(),id, username, firstName, middleName, lastName, patronymic, email,response);
        return ResponseEntity.ok("user has been updated");
    }

    @PatchMapping("/{id}/password")
    public ResponseEntity<TokenDto> changePassword(
            @PathVariable Long id,
            @RequestBody @Validated PasswordDto passwordDto,
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


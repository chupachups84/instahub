package vistar.practice.demo.controllers;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vistar.practice.demo.dtos.authentication.LoginDto;
import vistar.practice.demo.dtos.authentication.RegisterDto;
import vistar.practice.demo.dtos.token.TokenDto;
import vistar.practice.demo.services.AuthenticationService;

@RestController
@RequiredArgsConstructor
@RequestMapping("${auth.uri}")
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @GetMapping
    public ResponseEntity<String> confirmation(@RequestParam String token){
        authenticationService.confirm(token);
        return ResponseEntity.ok("email successfully confirmed");
    }

    @PostMapping("/register")
    public ResponseEntity<TokenDto> register(@RequestBody RegisterDto registerDto){
        return ResponseEntity.ok(authenticationService.register(registerDto));
    }

    @PostMapping("/login")
    public ResponseEntity<TokenDto> login(@RequestBody LoginDto loginDto){
        return ResponseEntity.ok(authenticationService.login(loginDto));
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request){
        return ResponseEntity.ok(authenticationService.logout(request));
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenDto> refreshAccessToken(HttpServletRequest request){
        return ResponseEntity.ok(authenticationService.refresh(request));
    }

    @PostMapping("/recover")
    public ResponseEntity<TokenDto> recoverUser(HttpServletRequest request){
        return ResponseEntity.ok(authenticationService.recover(request));
    }
}

package vistar.practice.demo.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
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

    @PostMapping()
    public ResponseEntity<TokenDto> activateUser(@RequestParam String token,HttpServletResponse response){
        return ResponseEntity.ok(authenticationService.activateUserByToken(token,response));
    }


    @PostMapping("/register")
    public ResponseEntity<String> register(
            @RequestBody @Validated RegisterDto registerDto
    ) {
        return ResponseEntity.ok(authenticationService.register(registerDto));
    }

    @PostMapping("/login")
    public ResponseEntity<TokenDto> login(
            @RequestBody LoginDto loginDto,
            HttpServletResponse response
    ) {
        return ResponseEntity.ok(authenticationService.login(loginDto,response));
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(
            HttpServletRequest request,
            HttpServletResponse response
    ){
        authenticationService.logout(request,response);
        return ResponseEntity.ok("user successfully logout");
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenDto> refreshAccessToken(
            HttpServletRequest request,
            HttpServletResponse response
    ){
        return ResponseEntity.ok(authenticationService.refresh(request,response));
    }

}

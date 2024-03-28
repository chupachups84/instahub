package vistar.practice.demo.services;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vistar.practice.demo.dtos.authentication.LoginDto;
import vistar.practice.demo.dtos.authentication.RegisterDto;
import vistar.practice.demo.dtos.token.TokenDto;
import vistar.practice.demo.models.UserEntity;
import vistar.practice.demo.repositories.UserRepository;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthenticationService {
    private final JwtTokenService jwtTokenService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;


    public TokenDto register(RegisterDto registerDto) {
        if (userRepository.existsByEmail(registerDto.getEmail())) {
            throw new RuntimeException("email already exist");
        }
        if (userRepository.existsByUsername(registerDto.getUsername())) {
            throw new RuntimeException("username already exist");
        }

        var user = userRepository.save(
                UserEntity.builder()
                        .firstName(registerDto.getFirstName())
                        .middleName(registerDto.getMiddleName())
                        .email(registerDto.getEmail())
                        .username(registerDto.getUsername())
                        .patronymic(registerDto.getPatronymic())
                        .lastName(registerDto.getLastName())
                        .password(passwordEncoder.encode(registerDto.getPassword()))
                        .build()
        );
        var accessToken = jwtTokenService.generateAccessToken(user);
        var refreshToken = jwtTokenService.generateRefreshToken(user);
        jwtTokenService.saveUserToken(accessToken, user);
        return TokenDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public TokenDto login(LoginDto loginDto) {
        var user = userRepository.findByUsername(loginDto.getUsername())
                .filter(UserEntity::isEnabled)
                .orElseThrow(
                        () -> new NoSuchElementException("user not found")
                );
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDto.getUsername(),
                        loginDto.getPassword()
                )
        );
        jwtTokenService.revokeAllUserToken(user);
        var accessToken = jwtTokenService.generateAccessToken(user);
        var refreshToken = jwtTokenService.generateRefreshToken(user);
        jwtTokenService.saveUserToken(accessToken, user);
        return TokenDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public String logout(HttpServletRequest request) {
        String token = request.getHeader(HttpHeaders.AUTHORIZATION).replace("Bearer ", "");
        var user = userRepository.findByUsername(jwtTokenService.extractUsername(token)).orElseThrow();
        jwtTokenService.revokeAllUserToken(user);
        SecurityContextHolder.clearContext();
        return "user successfully logout";
    }

    public TokenDto refresh(HttpServletRequest request) {
        String token = request.getHeader(HttpHeaders.AUTHORIZATION).replace("Bearer ", "");
        var user = userRepository.findByUsername(jwtTokenService.extractUsername(token)).orElseThrow();
        if (!jwtTokenService.isTokenValid(token, user)) {
            throw new IllegalStateException("token expired");
        }
        jwtTokenService.revokeAllUserToken(user);
        var accessToken = jwtTokenService.generateAccessToken(user);
        jwtTokenService.saveUserToken(accessToken, user);
        return TokenDto.builder()
                .accessToken(accessToken)
                .refreshToken(token)
                .build();
    }

    public TokenDto recover(HttpServletRequest request) {
        String token = request.getHeader(HttpHeaders.AUTHORIZATION).replace("Bearer ", "");
        if (token.isEmpty()) {
            throw new IllegalStateException("token required");
        }
        var user = userRepository.findByUsername(jwtTokenService.extractUsername(token)).orElseThrow();
        if (!jwtTokenService.isTokenValid(token, user)) {
            throw new IllegalStateException("token expired");
        }
        user.setActive(true);
        return TokenDto.builder()
                .accessToken(jwtTokenService.generateAccessToken(user))
                .refreshToken(jwtTokenService.generateRefreshToken(user))
                .build();
    }
}

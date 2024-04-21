package vistar.practice.demo.services;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vistar.practice.demo.dtos.authentication.LoginDto;
import vistar.practice.demo.dtos.authentication.RegisterDto;
import vistar.practice.demo.dtos.token.TokenDto;
import vistar.practice.demo.handler.exceptions.InvalidAccountException;
import vistar.practice.demo.handler.exceptions.RevokedTokenException;
import vistar.practice.demo.mappers.UserMapper;
import vistar.practice.demo.models.UserEntity;
import vistar.practice.demo.repositories.UserRepository;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional("transactionManager")
public class AuthenticationService {
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final UserMapper userMapper;
    private final MailService mailService;
    private static final String PREFIX = "Bearer ";


    public TokenDto register(RegisterDto registerDto) {
        registerDto.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        var user = userRepository.save(userMapper.toEntity(registerDto));
        String token = UUID.randomUUID().toString();
        mailService.saveConfirmationTokenMessage(user, token);
        mailService.sendConfirmationTokenMessage(user.getEmail(), token);
        return jwtService.getTokenDtoByUser(user);
    }

    public TokenDto login(LoginDto loginDto) {
        var user = userRepository.findByUsername(loginDto.getUsername())
                .filter(UserEntity::isEnabled)
                .orElseThrow(
                        () -> new UsernameNotFoundException("user not found")
                );
        if (!user.isEnabled())
            throw new InvalidAccountException("Account is invalid");
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDto.getUsername(),
                        loginDto.getPassword()
                )
        );
        jwtService.revokeAllUserToken(user);
        return jwtService.getTokenDtoByUser(user);
    }

    public String logout(HttpServletRequest request) {
        String token = request.getHeader(HttpHeaders.AUTHORIZATION).replace(PREFIX, "");
        var user = userRepository.findByUsername(jwtService.extractUsername(token)).orElseThrow();
        jwtService.revokeAllUserToken(user);
        SecurityContextHolder.clearContext();
        return "user successfully logout";
    }

    public TokenDto refresh(HttpServletRequest request) {
        String token = request.getHeader(HttpHeaders.AUTHORIZATION).replace(PREFIX, "");
        if (jwtService.isTokenRevoked(token)) {
            throw new RevokedTokenException("this token is already revoked");
        }
        var user = userRepository.findByUsername(jwtService.extractUsername(token)).orElseThrow();
        jwtService.revokeAllUserToken(user);
        return jwtService.getTokenDtoByUser(user);
    }

    public TokenDto recover(HttpServletRequest request) {
        String token = request.getHeader(HttpHeaders.AUTHORIZATION).replace(PREFIX, "");
        if (jwtService.isTokenRevoked(token)) {
            throw new RevokedTokenException("this token is already revoked");
        }
        var user = userRepository.findByUsername(jwtService.extractUsername(token)).orElseThrow();
        user.setActive(true);
        jwtService.revokeAllUserToken(user);
        return jwtService.getTokenDtoByUser(user);
    }

    public void confirm(String token) {
        mailService.confirmValidEmailTokenById(token);
    }
}

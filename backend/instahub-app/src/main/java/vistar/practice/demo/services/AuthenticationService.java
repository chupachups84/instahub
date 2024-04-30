package vistar.practice.demo.services;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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
import vistar.practice.demo.handler.exceptions.NoTokenException;
import vistar.practice.demo.handler.exceptions.RevokedTokenException;
import vistar.practice.demo.mappers.UserMapper;
import vistar.practice.demo.models.UserEntity;
import vistar.practice.demo.repositories.EmailTokenRepository;
import vistar.practice.demo.repositories.UserRepository;

import java.time.Instant;
import java.util.Arrays;
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
    private final EmailTokenRepository emailTokenRepository;
    private static final String PREFIX = "Bearer ";

    @Value("${email.token.expiration}")
    public Long confirmationExpiration;



    public String register(RegisterDto registerDto) {
        registerDto.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        var user = userMapper.toEntity(registerDto);
        user.setActive(false);
        var savedUser = userRepository.save(user);
        String token = UUID.randomUUID().toString();
        mailService.saveEmailToken(savedUser, token,confirmationExpiration);
        mailService.sendActivationAccountMessage(savedUser.getEmail(), token);
        return "a confirmation email has been sent to your email address";
    }

    public TokenDto login(
            LoginDto loginDto,
            HttpServletResponse response
    ) {
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
        var accessToken = jwtService.generateAccessToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        jwtService.saveUserToken(refreshToken, user);
        Cookie refreshCookie = new Cookie("refresh-token",refreshToken);
        response.addCookie(refreshCookie);
        return TokenDto.builder()
                .accessToken(accessToken)
                .build();
    }

    public void logout(HttpServletRequest request,HttpServletResponse response) {
        String token = request.getHeader(HttpHeaders.AUTHORIZATION).replace(PREFIX, "");
        var user = userRepository.findByUsername(jwtService.extractUsername(token)).orElseThrow();
        jwtService.revokeAllUserToken(user);
        SecurityContextHolder.clearContext();
        Cookie refreshCookie = new Cookie("refresh-token",null);
        response.addCookie(refreshCookie);
    }

    public TokenDto refresh(HttpServletRequest request,HttpServletResponse response) {
        var result = TokenDto.builder().build();
        Arrays.stream(request.getCookies()).filter(cookie -> cookie.getName().equals("refresh-token")).findFirst()
                .ifPresentOrElse(
                        cookie -> {
                            if (jwtService.isTokenRevoked(cookie.getValue())) {
                                response.addCookie(new Cookie("refresh-token",null));
                                throw new RevokedTokenException("this token is revoked");
                            } else {
                                var user = userRepository.findByUsername(
                                        jwtService.extractUsername(
                                                cookie.getValue()
                                        )
                                ).orElseThrow();
                                jwtService.revokeAllUserToken(user);
                                result.setAccessToken(jwtService.generateAccessToken(user));
                                var refreshToken = jwtService.generateRefreshToken(user);
                                jwtService.saveUserToken(refreshToken, user);
                                response.addCookie(new Cookie("refresh-token",refreshToken));
                            }
                        },
                        () -> {
                            throw new NoTokenException("cookie has no refresh token");
                        }
                );
        return result;
    }

    public String activateUserByToken(String token) {
        emailTokenRepository.findById(token)
                .filter(
                        eToken -> Instant.now().isBefore(eToken.getExpiresAt())
                )
                .filter(
                        eToken -> !eToken.isRevoked()
                ).ifPresentOrElse(
                        eToken -> {
                            eToken.setRevoked(true);
                            final var user =eToken.getUser();
                            user.setActive(true);
                        },
                        () -> {
                            throw new RevokedTokenException("invalid token");
                        }
                );
        return "you successfully activate your account";
    }
}

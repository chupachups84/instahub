package vistar.practice.demo.services;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
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
        mailService.saveEmailToken(savedUser, token, confirmationExpiration);
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
        Cookie refreshCookie = new Cookie("refresh_token", refreshToken);
        refreshCookie.setHttpOnly(true);
        refreshCookie.setMaxAge(31536000);
        refreshCookie.setPath("/");
        refreshCookie.setSecure(true);
        response.addCookie(refreshCookie);
        return TokenDto.builder()
                .accessToken(accessToken)
                .build();
    }

    public void logout(HttpServletRequest request, HttpServletResponse response) {
        String token = request.getHeader(HttpHeaders.AUTHORIZATION).replace(PREFIX, "");
        var user = userRepository.findByUsername(jwtService.extractUsername(token)).orElseThrow();
        jwtService.revokeAllUserToken(user);
        SecurityContextHolder.clearContext();

        Cookie[] cookies = request.getCookies();
        if (cookies!= null) {
            for (Cookie cookie : cookies) {
                if ("refresh_token".equals(cookie.getName())) {
                    cookie.setMaxAge(0);
                    cookie.setValue("");
                    cookie.setPath("/");
                    response.addCookie(cookie);
                }
            }
        }
    }

    public TokenDto refresh(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        log.info("Cookies: {}", Arrays.toString(cookies));
        if (cookies!= null) {
            for (Cookie cookie : cookies) {
                if ("refresh_token".equals(cookie.getName())) {
                    if (jwtService.isTokenRevoked(cookie.getValue())) {
                        cookie.setMaxAge(0);
                        cookie.setValue("");
                        cookie.setPath("/");
                        response.addCookie(cookie);
                        throw new RevokedTokenException("this token is revoked");
                    }
                    else {
                        var user = userRepository.findByUsername(jwtService.extractUsername(cookie.getValue()))
                                .orElseThrow();
                        jwtService.revokeAllUserToken(user);
                        var refreshToken = jwtService.generateRefreshToken(user);
                        jwtService.saveUserToken(refreshToken, user);
                        Cookie refreshCookie = new Cookie("refresh_token", refreshToken);
                        refreshCookie.setHttpOnly(true);
                        refreshCookie.setMaxAge(31536000);
                        refreshCookie.setPath("/");
                        refreshCookie.setSecure(true);
                        response.addCookie(refreshCookie);
                        log.info("cookie has been added {}",refreshCookie.toString());
                        return TokenDto.builder().accessToken(jwtService.generateAccessToken(user)).build();
                    }
                }
            }
        }
        throw new NoTokenException("error due refreshing");
    }

    public TokenDto activateUserByToken(String token, HttpServletResponse response) {
        final var tokenDto = TokenDto.builder().build();
        emailTokenRepository.findById(token)
                .filter(
                        eToken -> Instant.now().isBefore(eToken.getExpiresAt())
                )
                .filter(
                        eToken -> !eToken.isRevoked()
                ).ifPresentOrElse(
                        eToken -> {
                            eToken.setRevoked(true);
                            final var user = eToken.getUser();
                            user.setActive(true);
                            tokenDto.setAccessToken(jwtService.generateAccessToken(user));
                            final var refreshToken = jwtService.generateRefreshToken(user);
                            jwtService.saveUserToken(refreshToken, user);
                            Cookie refreshCookie = new Cookie("refresh_token", refreshToken);
                            refreshCookie.setHttpOnly(true);
                            refreshCookie.setSecure(true);
                            response.addCookie(refreshCookie);
                        },
                        () -> {
                            throw new RevokedTokenException("invalid token");
                        }
                );
        return tokenDto;
    }
}

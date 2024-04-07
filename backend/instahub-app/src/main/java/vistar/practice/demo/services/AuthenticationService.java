package vistar.practice.demo.services;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vistar.practice.demo.dtos.authentication.LoginDto;
import vistar.practice.demo.dtos.authentication.RegisterDto;
import vistar.practice.demo.dtos.mail.MailMessageDto;
import vistar.practice.demo.dtos.token.TokenDto;
import vistar.practice.demo.kafka.KafkaSender;
import vistar.practice.demo.mappers.UserMapper;
import vistar.practice.demo.models.UserEntity;
import vistar.practice.demo.repositories.UserRepository;

import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional("transactionManager")
public class AuthenticationService {
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private static final String PREFIX = "Bearer ";
    private static final String SUBJECT = "Confirmation message";
    @Value("${domain.address}")
    private String domain;
    @Value("${auth.uri}")
    private String uri;
    private static final String MESSAGE = "Click on the link to confirm your email ";

    private final UserMapper userMapper;
    private final KafkaSender kafkaSender;

    @Value("${kafka.topic.mail}")
   private String topic;


    public TokenDto register(RegisterDto registerDto) {
        if (userRepository.existsByEmail(registerDto.getEmail())) {
            throw new RuntimeException("email already exist");
        }
        if (userRepository.existsByUsername(registerDto.getUsername())) {
            throw new RuntimeException("username already exist");
        }
        String confirmToken = UUID.randomUUID().toString();
        kafkaSender.sendTransactionalMailMessage(
                topic,
                MailMessageDto.builder()
                        .email(registerDto.getEmail())
                        .subject(SUBJECT)
                        .message(MESSAGE+domain+"/"+uri+"?token="+confirmToken)
                        .build()
                );
        registerDto.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        var savedUser =userMapper.toEntity(registerDto);
        savedUser.setEmailConfirmationToken(confirmToken);
        var user = userRepository.save(savedUser);

        return jwtService.getTokenDtoByUser(user);
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
        var user = userRepository.findByUsername(jwtService.extractUsername(token)).orElseThrow();
        if (!jwtService.isTokenValid(token, user)) {
            throw new IllegalStateException("token expired");
        }
        jwtService.revokeAllUserToken(user);
        return jwtService.getTokenDtoByUser(user);
    }

    public TokenDto recover(HttpServletRequest request) {
        String token = request.getHeader(HttpHeaders.AUTHORIZATION).replace(PREFIX, "");
        if (token.isEmpty()) {
            throw new IllegalStateException("token required");
        }
        var user = userRepository.findByUsername(jwtService.extractUsername(token)).orElseThrow();
        if (!jwtService.isTokenValid(token, user)) {
            throw new IllegalStateException("token expired");
        }
        user.setActive(true);
        jwtService.revokeAllUserToken(user);
        return jwtService.getTokenDtoByUser(user);
    }

    public void confirm(String token) {
        userRepository.findByEmailConfirmationToken(token).ifPresentOrElse(
                u->u.setEmailConfirmationToken(null),
                ()->{
                    throw new IllegalStateException("invalid confirmation token");
                }
        );

    }
}

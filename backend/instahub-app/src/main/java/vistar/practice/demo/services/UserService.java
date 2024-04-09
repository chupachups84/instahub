package vistar.practice.demo.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vistar.practice.demo.dtos.token.TokenDto;
import vistar.practice.demo.dtos.user.PasswordDto;
import vistar.practice.demo.dtos.user.UserResponseDto;
import vistar.practice.demo.mappers.UserMapper;
import vistar.practice.demo.repositories.EmailTokenRepository;
import vistar.practice.demo.repositories.UserRepository;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional("transactionManager")
public class UserService {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final UserMapper userMapper;
    private final MailService mailService;
    private final PasswordEncoder passwordEncoder;
    private final EmailTokenRepository emailTokenRepository;

    @Value("${user.errors.not-found}")
    public String notFoundErrorText;


    public UserResponseDto findById(Long id) {
        return userMapper.toInfoDto(
                userRepository.findById(id).orElseThrow(
                        () -> new NoSuchElementException(notFoundErrorText)
                )
        );
    }

    public void updateUser(
            String userName,
            Long id,
            Optional<String> username,
            Optional<String> firstName,
            Optional<String> middleName,
            Optional<String> lastName,
            Optional<String> patronymic,
            Optional<String> email
    ) {
        var user = userRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException(notFoundErrorText)
        );
        if (!user.getUsername().equals(userName)) {
            throw new IllegalStateException("permission denied");
        }
        username.filter(s -> !s.trim().isEmpty()).ifPresent(
                s -> {
                    if (userRepository.existsByUsername(s)) {
                        throw new RuntimeException("username already exist");
                    }
                    user.setUsername(s);
                }
        );
        email.filter(s -> !s.trim().isEmpty()).ifPresent(
                s -> {
                    if (userRepository.existsByEmail(s)) {
                        throw new RuntimeException("email already exist");
                    }
                    String token = UUID.randomUUID().toString();
                    mailService.saveConfirmationTokenMessage(user, token);
                    mailService.sendConfirmationTokenMessage(s, token);
                    user.setEmail(s);
                }
        );
        firstName.filter(s -> !s.trim().isEmpty()).ifPresent(user::setFirstName);
        middleName.filter(s -> !s.trim().isEmpty()).ifPresent(user::setMiddleName);
        lastName.filter(s -> !s.trim().isEmpty()).ifPresent(user::setLastName);
        patronymic.filter(s -> !s.trim().isEmpty()).ifPresent(user::setPatronymic);

    }

    public TokenDto deleteUser(
            Long id,
            String userName
    ) {
        var user = userRepository.findById(id).orElseThrow(() -> new NoSuchElementException(notFoundErrorText));
        if (!user.getUsername().equals(userName)) {
            throw new IllegalStateException("permission denied");
        }
        user.setActive(false);
        SecurityContextHolder.clearContext();
        jwtService.revokeAllUserToken(user);
        return TokenDto.builder()
                .accessToken(jwtService.generateAccessToken(user))
                .refreshToken(jwtService.generateRefreshToken(user))
                .build();
    }

    public TokenDto changePassword(Long id, PasswordDto passwordDto) {
        var user = userRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException(notFoundErrorText)
        );
        if (!passwordEncoder.matches(passwordDto.getOldPassword(), user.getPassword())) {
            throw new IllegalStateException("invalid password");
        }
        if (!passwordDto.getNewPassword().equals(passwordDto.getConfirmNewPassword())) {
            throw new IllegalStateException("passwords don't match");
        }
        user.setPassword(passwordEncoder.encode(passwordDto.getNewPassword()));
        jwtService.revokeAllUserToken(user);
        return TokenDto.builder()
                .accessToken(jwtService.generateAccessToken(user))
                .refreshToken(jwtService.generateRefreshToken(user))
                .build();
    }
}

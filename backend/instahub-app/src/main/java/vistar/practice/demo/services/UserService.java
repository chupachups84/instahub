package vistar.practice.demo.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vistar.practice.demo.dtos.token.TokenDto;
import vistar.practice.demo.dtos.user.UserResponseDto;
import vistar.practice.demo.mappers.UserMapper;
import vistar.practice.demo.repositories.UserRepository;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional("transactionManager")
public class UserService {
    private final UserRepository userRepository;
    private final JwtTokenService jwtTokenService;
    private final UserMapper userMapper;

    @Value("${user.uri.errors.not-found}")
    public static String notFoundErrorText;

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
            Optional<String> email,
            Optional<String> password
    ) {
        var user = userRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException(notFoundErrorText)
        );
        if (!user.getUsername().equals(userName)) {
            throw new IllegalStateException("permission denied");
        }
        username.filter(s -> !s.trim().isEmpty()).ifPresent(user::setUsername);
        email.filter(s -> !s.trim().isEmpty()).ifPresent(user::setEmail);
        password.filter(s -> !s.trim().isEmpty()).ifPresent(user::setPassword);
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
        return TokenDto.builder()
                .accessToken(jwtTokenService.generateAccessToken(user))
                .refreshToken(jwtTokenService.generateRefreshToken(user))
                .build();
    }
}

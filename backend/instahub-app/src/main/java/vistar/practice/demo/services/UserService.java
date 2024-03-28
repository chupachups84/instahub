package vistar.practice.demo.services;

import lombok.RequiredArgsConstructor;
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
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final JwtTokenService jwtTokenService;


    public UserResponseDto findById(Long id) {
        return UserMapper.toInfoDto(
                userRepository.findById(id).orElseThrow(
                        () -> new NoSuchElementException("user not found")
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
                () -> new NoSuchElementException("user not found")
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

    @Transactional
    public TokenDto deleteUser(
            Long id,
            String userName
    ) {
        var user = userRepository.findById(id).orElseThrow(() -> new NoSuchElementException("user not found"));
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

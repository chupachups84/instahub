package vistar.practice.demo.services;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vistar.practice.demo.dtos.token.TokenDto;
import vistar.practice.demo.dtos.user.PasswordDto;
import vistar.practice.demo.dtos.user.UserResponseDto;
import vistar.practice.demo.handler.exceptions.NotUniqueEmailException;
import vistar.practice.demo.handler.exceptions.NotUniqueUsernameException;
import vistar.practice.demo.mappers.UserMapper;
import vistar.practice.demo.models.UserEntity;
import vistar.practice.demo.repositories.UserRepository;

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

    @Value("${user.errors.not-found}")
    public String notFoundErrorText;

    public UserResponseDto findById(Long id) {
        return userMapper.toInfoDto(
                userRepository.findById(id)
                        .filter(UserEntity::isEnabled).orElseThrow(
                        () -> new UsernameNotFoundException(notFoundErrorText)
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
            HttpServletResponse response
    ) {
        var user = userRepository.findById(id).orElseThrow(
                () -> new UsernameNotFoundException(notFoundErrorText)
        );
        if (!user.getUsername().equals(userName)) {
            throw new AccessDeniedException("permission denied");
        }
        username.filter(s -> !s.trim().isEmpty()).ifPresent(
                s-> {
                    if(userRepository.existsByUsername(s)){
                        throw new NotUniqueUsernameException("username already exist");
                    }
                    jwtService.revokeAllUserToken(user);
                    user.setUsername(s);
                    response.addCookie(new Cookie("refresh-token",null));
                    SecurityContextHolder.clearContext();
                }
        );
        email.filter(s -> !s.trim().isEmpty()).ifPresent(
                s->{
                    if(userRepository.existsByEmail(s)){
                        throw new NotUniqueEmailException("email already exist");
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

    public void deleteUser(
            Long id,
            String userName,
            HttpServletResponse response
    ) {
        var user = userRepository.findById(id).orElseThrow(
                () -> new UsernameNotFoundException(notFoundErrorText)
        );
        if (!user.getUsername().equals(userName)) {
            throw new AccessDeniedException("permission denied");
        }
        user.setActive(false);
        SecurityContextHolder.clearContext();
        jwtService.revokeAllUserToken(user);
        response.addCookie(new Cookie("refresh-token",null));
        String recoverToken = UUID.randomUUID().toString();
        mailService.saveRecoverTokenMessage(user,recoverToken);
        mailService.sendRecoverTokenMessage(user.getEmail(),recoverToken);
    }

    public TokenDto changePassword(
            Long id,
            PasswordDto passwordDto,
            String userName,
            HttpServletResponse response
    ) {
        var user = userRepository.findById(id).orElseThrow(
                ()-> new UsernameNotFoundException(notFoundErrorText)
        );
        if (!user.getUsername().equals(userName)) {
            throw new AccessDeniedException("permission denied");
        }
        user.setPassword(passwordEncoder.encode(passwordDto.getNewPassword()));
        jwtService.revokeAllUserToken(user);
        var accessToken = jwtService.generateAccessToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        jwtService.saveUserToken(refreshToken, user);
        response.addCookie(new Cookie("refresh-token",refreshToken));
        return TokenDto.builder()
                .accessToken(accessToken)
                .build();
    }
}

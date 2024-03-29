package vistar.practice.demo.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vistar.practice.demo.dtos.user.UserRequestDto;
import vistar.practice.demo.dtos.user.UserResponseDto;
import vistar.practice.demo.mappers.UserMapper;
import vistar.practice.demo.models.UserEntity;
import vistar.practice.demo.repositories.UserRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional("transactionManager")
public class UserService {
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<UserResponseDto> findAll() {
        return userRepository.findAll().stream().filter(UserEntity::isActive).map(UserMapper::toInfoDto).toList();
    }

    public void createUser(UserRequestDto request) {
        if (userRepository.existsByEmail(request.getEmail()) || userRepository.existsByUsername(request.getUsername())
        ) {
            throw new RuntimeException("email or username already exist");
        }
        userRepository.save(UserMapper.toEntity(request));
    }

    public UserResponseDto findById(Long id) {
        return UserMapper.toInfoDto(
                userRepository.findById(id).orElseThrow(
                        () -> new NoSuchElementException("user not found")
                )
        );
    }

    public void updateUser(
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
        username.filter(s -> !s.trim().isEmpty()).ifPresent(user::setUsername);
        email.filter(s -> !s.trim().isEmpty()).ifPresent(user::setEmail);
        password.filter(s -> !s.trim().isEmpty()).ifPresent(user::setPassword);
        firstName.filter(s -> !s.trim().isEmpty()).ifPresent(user::setFirstName);
        middleName.filter(s -> !s.trim().isEmpty()).ifPresent(user::setMiddleName);
        lastName.filter(s -> !s.trim().isEmpty()).ifPresent(user::setLastName);
        patronymic.filter(s -> !s.trim().isEmpty()).ifPresent(user::setPatronymic);

    }

    public void deleteUser(Long id) {
        userRepository.findById(id).ifPresentOrElse(
                user -> user.setActive(false),
                () -> new NoSuchElementException("user not found")
        );
    }
}

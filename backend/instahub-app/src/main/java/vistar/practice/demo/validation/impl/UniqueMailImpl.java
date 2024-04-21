package vistar.practice.demo.validation.impl;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vistar.practice.demo.dtos.authentication.RegisterDto;
import vistar.practice.demo.repositories.UserRepository;

import vistar.practice.demo.validation.UniqueMail;

@Component
@RequiredArgsConstructor
public class UniqueMailImpl implements ConstraintValidator<UniqueMail, String> {
    private final UserRepository userRepository;
    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        return !userRepository.existsByEmail(email);
    }
}

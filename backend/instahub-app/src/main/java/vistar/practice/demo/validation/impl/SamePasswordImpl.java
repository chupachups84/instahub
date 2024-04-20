package vistar.practice.demo.validation.impl;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import vistar.practice.demo.dtos.user.PasswordDto;
import vistar.practice.demo.validation.SamePassword;

public class SamePasswordImpl implements ConstraintValidator<SamePassword, PasswordDto> {
    @Override
    public boolean isValid(PasswordDto dto, ConstraintValidatorContext context) {
        return dto.getNewPassword().equals(dto.getConfirmNewPassword());
    }
}

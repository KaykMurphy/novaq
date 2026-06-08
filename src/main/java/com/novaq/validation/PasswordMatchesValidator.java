package com.novaq.validation;

import com.novaq.dtos.request.UserRegisterDTO;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, UserRegisterDTO> {

    @Override
    public boolean isValid(UserRegisterDTO dto, ConstraintValidatorContext context) {
        return dto.getSenha() != null &&
                dto.getSenha().equals(dto.getConfirmSenha());
    }
}

package io.aycodes.automataapi.common.validations.password;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordValidator implements ConstraintValidator<PasswordConstraint, String> {

    @Override
    public void initialize(final PasswordConstraint passwordConstraint) {
    }

    @Override
    public boolean isValid(final String password, final ConstraintValidatorContext constraint) {
        return password != null && password.matches("(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[^\\w\\s]).{8,20}$");
    }
}

package dk.minkostplan.backend.constraints;

import dk.minkostplan.backend.payload.request.RegisterRequest;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordMatchConfirmPasswordValidator implements ConstraintValidator<PasswordMatchConfirmPassword, RegisterRequest> {
    @Override
    public boolean isValid(RegisterRequest value, ConstraintValidatorContext context) {
        return value.getPassword().equals(value.getConfirmPassword());
    }
}

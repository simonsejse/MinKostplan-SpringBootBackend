package dk.minkostplan.backend.constraints;

import org.springframework.validation.annotation.Validated;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target(value= ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PasswordMatchConfirmPasswordValidator.class)
@Documented
public @interface PasswordMatchConfirmPassword {
    String message() default "Din bekræftede adgangskode matcher ikke den originale adgangskode.";
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default { };
}

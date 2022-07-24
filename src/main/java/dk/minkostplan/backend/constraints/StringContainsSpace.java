package dk.minkostplan.backend.constraints;

import org.springframework.validation.annotation.Validated;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.Valid;
import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = StringContainsSpaceValidator.class)
public @interface StringContainsSpace {
    String message() default "Du mangler et mellemrum!";
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default { };
}

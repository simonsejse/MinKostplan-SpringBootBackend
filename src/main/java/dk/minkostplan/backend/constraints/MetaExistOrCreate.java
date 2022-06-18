package dk.minkostplan.backend.constraints;


import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = MetaExistOrCreateValidator.class)
public @interface MetaExistOrCreate {
    String message() default "Kunne ikke tilføje det valgte opskrift tag!";
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default { };
}

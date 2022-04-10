package dk.minkostplan.backend.constraints;


import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = MetaExistValidator.class)
public @interface MetaExist {
    String message() default "Du har brugt meta data i din opskrift som ikke findes i databasen!";
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default { };
}

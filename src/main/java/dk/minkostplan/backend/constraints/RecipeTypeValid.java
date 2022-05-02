package dk.minkostplan.backend.constraints;

import dk.minkostplan.backend.models.RecipeType;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

import static java.lang.annotation.ElementType.*;

@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = RecipeTypeValidator.class)
public @interface RecipeTypeValid {
    String message() default "Det er ikke en tilladt opskrift type!";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

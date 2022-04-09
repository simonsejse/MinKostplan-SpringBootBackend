package dk.minkostplan.backend.constraints;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE_USE;

@Target( { TYPE_USE } )
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = NotMacrosAndCaloriesValidator.class)
public @interface NotMacrosAndCalories {
    String message() default "Du kan ikke både have (protein, kulhydrater og fedt) sammen med dine kalorier da de hænger sammen! Vælg en af delene eller bland dem!";
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default { };
}

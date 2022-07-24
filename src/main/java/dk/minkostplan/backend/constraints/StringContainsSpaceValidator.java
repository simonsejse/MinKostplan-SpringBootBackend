package dk.minkostplan.backend.constraints;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class StringContainsSpaceValidator implements ConstraintValidator<StringContainsSpace, String> {
   public void initialize(StringContainsSpace constraint) {
   }

   public boolean isValid(String obj, ConstraintValidatorContext context) {
      return obj.contains(" ");
   }

}

package dk.minkostplan.backend.constraints;

import dk.minkostplan.backend.models.RecipeType;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.constraints.Null;

public class RecipeTypeValidator implements ConstraintValidator<RecipeTypeValid, String> {
   public void initialize(RecipeTypeValid constraint) {
   }

   public boolean isValid(String type, ConstraintValidatorContext context) {
      try{
         RecipeType.valueOf(type);
         return true;
      }catch(IllegalArgumentException | NullPointerException e) {
         return false;
      }
   }


}

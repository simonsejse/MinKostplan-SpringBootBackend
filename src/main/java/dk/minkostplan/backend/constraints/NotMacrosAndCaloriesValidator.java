package dk.minkostplan.backend.constraints;

import dk.minkostplan.backend.payload.request.NutritionalValuesRequest;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class NotMacrosAndCaloriesValidator implements ConstraintValidator<NotMacrosAndCalories, NutritionalValuesRequest> {

    @Override
    public boolean isValid(NutritionalValuesRequest nutritionalReq, ConstraintValidatorContext context) {
        Float calories = nutritionalReq.getCalories();
        Float protein = nutritionalReq.getProtein();
        Float carbs = nutritionalReq.getCarbs();
        Float fat = nutritionalReq.getFat();

        boolean allSet = calories != null && protein != null && carbs != null && fat != null;
        return !allSet;
    }
}

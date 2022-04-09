package dk.minkostplan.backend.payload.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import dk.minkostplan.backend.constraints.NotMacrosAndCalories;
import dk.minkostplan.backend.models.Role;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Data
@AllArgsConstructor
@NotMacrosAndCalories
public class NutritionalValuesRequest {
    @Min(value=10, message = "Det giver ikke mening at have et måltid under 10 kalorier!")
    private Float calories;
    @PositiveOrZero(message = "Protein kan ikke være i minus!")
    private Float protein;
    @PositiveOrZero(message = "Fedt kan ikke være i minus!")
    private Float fat;
    @PositiveOrZero(message = "Kulhydrater kan ikke være i minus!")
    private Float carbs;
}

package dk.minkostplan.backend.models.dtos.recipes;

import com.fasterxml.jackson.annotation.JsonProperty;
import dk.minkostplan.backend.entities.Macros;
import dk.minkostplan.backend.payload.request.NutritionalValuesRequest;
import lombok.Data;

@Data
public class MacroDTO {
    @JsonProperty("default")
    private MacroDistributionDTO normal;
    @JsonProperty("wanted")
    private MacroDistributionDTO wanted;

    public MacroDTO(Macros macros, NutritionalValuesRequest nutritionalValuesRequest){
        this.normal = new MacroDistributionDTO.Builder()
                .calories(macros.getCalories())
                .protein(macros.getProtein())
                .fat(macros.getFat())
                .carbs(macros.getCarbs())
                .build();

        Float wantedCalories = nutritionalValuesRequest.getCalories();
        Float wantedProtein = nutritionalValuesRequest.getProtein();
        Float wantedFat = nutritionalValuesRequest.getFat();
        Float wantedCarbs = nutritionalValuesRequest.getCarbs();

        this.wanted = new MacroDistributionDTO.Builder()
                .calories(wantedCalories != null ? wantedCalories : macros.getCalories())
                .protein(wantedProtein != null ? wantedProtein : macros.getProtein())
                .fat(wantedFat != null ? wantedFat : macros.getFat())
                .carbs(wantedCarbs != null ? wantedCarbs : macros.getCarbs())
                .build();
    }
}

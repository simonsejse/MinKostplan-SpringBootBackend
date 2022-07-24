package dk.minkostplan.backend.payload.response.recipes;

import com.fasterxml.jackson.annotation.JsonProperty;
import dk.minkostplan.backend.entities.Macros;
import lombok.Data;

import java.util.Optional;

@Data
public class MacroDTO {
    @JsonProperty("default")
    private MacroDistributionDTO normal;
    @JsonProperty("wanted")
    private MacroDistributionDTO wanted;

    public MacroDTO(Macros macros, Optional<Integer> calories){
        final Integer defaultCalories = macros.getCalories();

        this.normal = new MacroDistributionDTO.Builder()
                .calories(defaultCalories)
                .protein(macros.getProtein())
                .fat(macros.getFat())
                .carbs(macros.getCarbs())
                .build();


        Integer wantedCalories = calories.orElse(defaultCalories);
        float wantedProtein = macros.getProtein() * (wantedCalories / defaultCalories);
        float wantedFat = macros.getFat() * (wantedCalories / defaultCalories);
        float wantedCarbs = macros.getCarbs() * (wantedCalories / defaultCalories);

        this.wanted = new MacroDistributionDTO.Builder()
                .calories(wantedCalories)
                .protein(wantedProtein)
                .fat(wantedFat)
                .carbs(wantedCarbs)
                .build();
    }
}

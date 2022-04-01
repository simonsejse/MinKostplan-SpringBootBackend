package dk.minkostplan.backend.models.dtos.recipes;

import com.fasterxml.jackson.annotation.JsonProperty;
import dk.minkostplan.backend.entities.RecipeInstruction;
import lombok.Data;

@Data
public class AnalysedInstructionDTO {
    @JsonProperty("id")
    private long id;
    @JsonProperty("number")
    private int number;
    @JsonProperty("step")
    private String step;
    @JsonProperty("fk_recipe_id")
    private long fk_recipe_id;

    public AnalysedInstructionDTO(RecipeInstruction recipeInstruction){
        this.id = recipeInstruction.getId();
        this.number = recipeInstruction.getNumber();
        this.step = recipeInstruction.getStep();
        this.fk_recipe_id = recipeInstruction.getRecipe().getId();
    }
}

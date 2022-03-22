package dk.minkostplan.backend.models.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import dk.minkostplan.backend.entities.Recipe;
import dk.minkostplan.backend.entities.MealInstruction;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;
@Getter
@Setter
public class RecipeDTO {
    @JsonProperty(value="meal_id")
    private long id;
    @JsonProperty(value="meal_name")
    private String name;
    @JsonProperty(value="diet_type")
    private String mealType;
    @JsonProperty(value="ingredients")
    private final List<FoodDTO> ingredients;
    @JsonProperty(value="instructions")
    private final List<String> instructions;

    public RecipeDTO(Recipe recipe){
        this.id = recipe.getRecipeId();
        this.name = recipe.getName();
        this.mealType = recipe.getType().name();

        this.ingredients = recipe.getIngredients()
                .stream()
                .map(FoodDTO::new)
                .collect(Collectors.toList());

        this.instructions = recipe.getInstructions()
                .stream()
                .map(MealInstruction::getInstruction)
                .collect(Collectors.toList());
    }
}

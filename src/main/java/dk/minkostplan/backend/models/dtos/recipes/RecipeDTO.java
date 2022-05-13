package dk.minkostplan.backend.models.dtos.recipes;

import com.fasterxml.jackson.annotation.JsonProperty;
import dk.minkostplan.backend.entities.Ingredient;
import dk.minkostplan.backend.entities.Recipe;
import dk.minkostplan.backend.entities.RecipeInstruction;
import dk.minkostplan.backend.models.RecipeApproval;
import dk.minkostplan.backend.models.RecipeType;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SortNatural;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
@Setter
public class RecipeDTO {
    @JsonProperty(value="id")
    private long id;
    @JsonProperty(value="name")
    private String name;
    @JsonProperty(value="description")
    private String description;
    @JsonProperty(value="approval")
    private RecipeApproval recipeApproval;
    @JsonProperty(value="type")
    private RecipeType recipeType;
    @JsonProperty(value="vegetarian")
    private Boolean vegetarian;
    @JsonProperty(value="vegan")
    private Boolean vegan;
    @JsonProperty(value="glutenFree")
    private Boolean glutenFree;
    @JsonProperty(value="dairyFree")
    private Boolean dairyFree;
    @JsonProperty(value="veryHealthy")
    private Boolean veryHealthy;
    @JsonProperty(value="cheap")
    private Boolean cheap;
    @JsonProperty(value="veryPopular")
    private Boolean veryPopular;
    @JsonProperty(value="sustainable")
    private Boolean sustainable;
    @JsonProperty(value="pricePerServing")
    private float pricePerServing;
    @JsonProperty(value="instructions")
    private String instructions;
    @JsonProperty(value="readyInMinutes")
    private Integer readyInMinutes;
    @JsonProperty(value="image")
    private String image;
    @JsonProperty("macros")
    private MacroDTO macros;
    @JsonProperty(value="ingredients")
    private List<IngredientDTO> ingredients;
    @JsonProperty(value="analyzedInstructions")
    private List<AnalysedInstructionDTO> analyzedInstructions;

    public RecipeDTO(Recipe recipe){
        this.id = recipe.getId();
        this.name = recipe.getName();
        this.description = recipe.getDescription();
        this.recipeApproval = recipe.getApproval();
        this.recipeType = recipe.getType();
        this.vegetarian = recipe.getVegetarian();
        this.vegan = recipe.getVegan();
        this.glutenFree = recipe.getGlutenFree();
        this.dairyFree = recipe.getDairyFree();
        this.veryHealthy = recipe.getVeryHealthy();
        this.cheap = recipe.getCheap();
        this.veryPopular = recipe.getVeryPopular();
        this.sustainable = recipe.getSustainable();
        this.pricePerServing = recipe.getPricePerServing();
        this.instructions = recipe.getInstructions();
        this.readyInMinutes = recipe.getReadyInMinutes();
        this.image = recipe.getImage();
    }
}

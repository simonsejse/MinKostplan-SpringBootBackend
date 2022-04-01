package dk.minkostplan.backend.models.dtos.recipes;

import com.fasterxml.jackson.annotation.JsonProperty;
import dk.minkostplan.backend.entities.Ingredient;
import dk.minkostplan.backend.entities.Recipe;
import dk.minkostplan.backend.entities.RecipeInstruction;
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
    @JsonProperty(value="mealId")
    private long id;
    @JsonProperty(value="mealName")
    private String name;
    @JsonProperty(value="mealType")
    private String mealType;
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
    private Double pricePerServing;
    @JsonProperty(value="instructions")
    private String instructions;
    @JsonProperty(value="serving")
    private Integer serving;
    @JsonProperty(value="readyInMinutes")
    private Integer readyInMinutes;
    @JsonProperty(value="ingredients")
    private final List<IngredientDTO> ingredients;
    @JsonProperty(value="analyzedInstructions")
    private final List<AnalysedInstructionDTO> analyzedInstructions;



    public RecipeDTO(Recipe recipe){
        this.id = recipe.getId();
        this.name = recipe.getName();
        this.mealType = recipe.getType();
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
        this.serving = recipe.getServing();
        this.readyInMinutes = recipe.getReadyInMinutes();
        System.out.println(recipe.getIngredients().size());
        this.ingredients = recipe.getIngredients().stream().map(in -> {
            System.out.println(in);
            return new IngredientDTO(in);
        }).collect(Collectors.toList());
        this.analyzedInstructions = recipe.getAnalyzedInstructions().stream().map(AnalysedInstructionDTO::new).collect(Collectors.toList());


    }
}

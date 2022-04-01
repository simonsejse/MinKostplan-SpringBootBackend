package dk.minkostplan.backend.models.dtos.recipes;

import com.fasterxml.jackson.annotation.JsonProperty;
import dk.minkostplan.backend.entities.Ingredient;
import dk.minkostplan.backend.entities.Meta;
import lombok.Data;

import java.util.Set;
import java.util.stream.Collectors;

@Data
public class IngredientDTO {
    @JsonProperty("food")
    private final FoodDTO foodDTO;
    @JsonProperty("id")
    private Long id;
    @JsonProperty("amount")
    private float amount;
    @JsonProperty("instruction")
    private String instruction;
    @JsonProperty("unit")
    private String unit;
    @JsonProperty("metas")
    private Set<String> metas;

    public IngredientDTO(Ingredient ingredient){
        this.foodDTO = new FoodDTO(ingredient.getFood());
        this.id = ingredient.getId();
        this.amount = ingredient.getAmount();
        this.instruction = ingredient.getInstruction();
        this.unit = ingredient.getUnit();
        this.metas = ingredient.getMeta().stream().map(Meta::getMeta).collect(Collectors.toSet());
    }
}

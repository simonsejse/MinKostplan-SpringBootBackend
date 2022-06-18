package dk.minkostplan.backend.payload.response.recipes;

import com.fasterxml.jackson.annotation.JsonProperty;
import dk.minkostplan.backend.entities.Ingredient;
import dk.minkostplan.backend.entities.Measurement;
import lombok.Data;

@Data
public class IngredientDTO {
    @JsonProperty("food")
    private final FoodDTO foodDTO;
    @JsonProperty("id")
    private Long id;
    @JsonProperty("amount")
    private float amount;
    @JsonProperty("measures")
    private MeasurementsDTO measurementsDTO;


    public IngredientDTO(Ingredient ingredient){
        this.foodDTO = new FoodDTO(ingredient.getFood());
        this.id = ingredient.getId();
        this.amount = ingredient.getAmount();

        final Measurement measure = ingredient.getMeasure();
        this.measurementsDTO = MeasurementsDTO.builder()
                .id(measure.getId())
                .type(measure.getType().getName())
                .amountOfType(measure.getAmountOfType())
                .amountInGrams(measure.getAmountInGrams())
                .build();
    }
}

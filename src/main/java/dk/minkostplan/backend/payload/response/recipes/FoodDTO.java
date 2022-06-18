package dk.minkostplan.backend.payload.response.recipes;

import com.fasterxml.jackson.annotation.JsonProperty;
import dk.minkostplan.backend.entities.Food;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FoodDTO {
    @JsonProperty(value="food_id")
    private Long id;
    @JsonProperty(value="food_type")
    private String foodType;
    @JsonProperty(value="food_name")
    private String name;
    @JsonProperty(value="food_kj")
    private float kj;
    @JsonProperty(value="food_kcal")
    private float kcal;
    @JsonProperty(value="food_protein")
    private float protein;
    @JsonProperty(value="food_carbs")
    private float carbs;
    @JsonProperty(value="food_fat")
    private float fat;
    @JsonProperty(value="food_added_sugars")
    private float addedSugars;
    @JsonProperty(value="food_fibers")
    private float fibers;
    
    public FoodDTO(Food entity){
        this.id = entity.getId();
        this.foodType = entity.getFoodType();
        this.name = entity.getName();
        this.kj = entity.getKj();
        this.kcal = entity.getKcal();
        this.protein = entity.getProtein();
        this.carbs = entity.getCarbs();
        this.fat = entity.getFat();
        this.addedSugars = entity.getAddedSugars();
        this.fibers = entity.getFibers();
    }
    
    protected FoodDTO(){}
}

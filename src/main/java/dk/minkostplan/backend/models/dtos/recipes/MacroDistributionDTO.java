package dk.minkostplan.backend.models.dtos.recipes;

import lombok.Data;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.Column;

@Data
public class MacroDistributionDTO {
    @Column(name="calories")
    private float calories;
    @Column(name="fat")
    private float fat;
    @Column(name="protein")
    private float protein;
    @Column(name="carbs")
    private float carbs;

    public MacroDistributionDTO(Builder builder) {
        this.calories = builder.calories;
        this.fat = builder.fat;
        this.protein = builder.protein;
        this.carbs = builder.carbs;
    }

    @Accessors(fluent = true)
    @Setter
    public static class Builder{
        private float calories;
        private float fat;
        private float protein;
        private float carbs;

        public MacroDistributionDTO build(){
            return new MacroDistributionDTO(this);
        }
    }
}

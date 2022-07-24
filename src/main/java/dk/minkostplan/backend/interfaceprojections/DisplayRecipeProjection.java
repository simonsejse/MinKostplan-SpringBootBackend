package dk.minkostplan.backend.interfaceprojections;

import com.fasterxml.jackson.annotation.JsonIgnoreType;
import lombok.Data;
import lombok.Getter;
public interface DisplayRecipeProjection {
    Long getId();
    String getName();
    String getDescription();
    Integer getCalories();
    Integer getFat();
    Integer getProtein();
    Integer getCarbs();
    Integer getPricePerServing();
    Integer getReadyInMinutes();
    String getImage();
    String getCreatedBy();

    @JsonIgnoreType
    @Getter
    @Data
    class DTO {
        private Long id;
        private String name;
        private String description;
        private Integer calories;
        private Integer fat;
        private Integer protein;
        private Integer carbs;
        private Integer pricePerServing;
        private Integer readyInMinutes;
        private String image;
        private String createdBy;
        private float ratio; /* Can mbe used as a way of showing portions: 0,4/1 portion and if more 2/1 portion etc..*/

        public DTO(DisplayRecipeProjection displayRecipeProjection, Integer wantedCalories){
            this.id = displayRecipeProjection.getId();
            this.name = displayRecipeProjection.getName();
            this.description = displayRecipeProjection.getDescription();
            this.ratio = (float) wantedCalories / displayRecipeProjection.getCalories();
            this.calories = wantedCalories;
            this.protein = (int) (displayRecipeProjection.getProtein() * this.ratio);
            this.carbs = (int) (displayRecipeProjection.getCarbs() * this.ratio);
            this.fat = (int) (displayRecipeProjection.getFat() * this.ratio);
            this.pricePerServing = (int) (displayRecipeProjection.getPricePerServing() * this.ratio);
            this.readyInMinutes = (int) (displayRecipeProjection.getReadyInMinutes() * this.ratio);
            this.image = displayRecipeProjection.getImage();
            this.createdBy = displayRecipeProjection.getCreatedBy();
        }

        public DTO(DisplayRecipeProjection displayRecipeProjection){
            this(displayRecipeProjection, displayRecipeProjection.getCalories());
        }
    }
}
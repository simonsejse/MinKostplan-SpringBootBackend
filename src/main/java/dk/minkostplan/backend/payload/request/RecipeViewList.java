package dk.minkostplan.backend.payload.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import dk.minkostplan.backend.entities.Recipe;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecipeViewList {
    @JsonProperty("id")
    private Long id;
    @JsonProperty("name")
    private String name;
    @JsonProperty("description")
    private String description;
    @JsonProperty("createdBy")
    private String createdBy;

    public RecipeViewList(Recipe recipe) {
        this.id = recipe.getId();
        this.name = recipe.getName();
        this.description = recipe.getDescription();
        this.createdBy = recipe.getCreatedBy().getEmail();
    }
}

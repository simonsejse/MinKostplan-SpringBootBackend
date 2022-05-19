package dk.minkostplan.backend.payload.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import dk.minkostplan.backend.entities.Recipe;
import lombok.Data;

@Data
public class RecipesPendingDTO {
    @JsonProperty("id")
    private Long id;
    @JsonProperty("name")
    private String name;
    @JsonProperty("description")
    private String description;
    @JsonProperty("createdBy")
    private String createdBy;
    @JsonProperty("upvotes")
    private int upvotes;
    @JsonProperty("downvotes")
    private int downvotes;


    protected RecipesPendingDTO() { }

    public RecipesPendingDTO(Recipe recipe, int upvotes, int downvotes) {
        this.id = recipe.getId();
        this.name = recipe.getName();
        this.description = recipe.getDescription();
        this.createdBy = recipe.getCreatedBy().getEmail();
        this.upvotes = upvotes;
        this.downvotes = downvotes;
    }
}

package dk.minkostplan.backend.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class RecipeVoteId implements Serializable {

    @JoinColumn(name = "recipe_id")
    @ManyToOne(fetch=FetchType.LAZY)
    private Recipe recipe;

    @JoinColumn(name = "user_id")
    @ManyToOne(fetch= FetchType.LAZY)
    private User submittedBy;

    protected RecipeVoteId(){}

    public RecipeVoteId(Recipe recipe, User submittedBy) {
        this.recipe = recipe;
        this.submittedBy = submittedBy;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public User getSubmittedBy() {
        return submittedBy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RecipeVoteId)) return false;
        RecipeVoteId that = (RecipeVoteId) o;
        return Objects.equals(getRecipe(), that.getRecipe())
                &&
                Objects.equals(getSubmittedBy(), that.getSubmittedBy());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getRecipe(), getSubmittedBy());
    }
}

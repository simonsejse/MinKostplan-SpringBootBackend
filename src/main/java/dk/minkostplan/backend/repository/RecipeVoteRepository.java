package dk.minkostplan.backend.repository;

import dk.minkostplan.backend.entities.Recipe;
import dk.minkostplan.backend.entities.RecipeVote;
import dk.minkostplan.backend.entities.RecipeVoteId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RecipeVoteRepository extends JpaRepository<RecipeVote, RecipeVoteId> {
    @Query("SELECT count(rv) FROM RecipeVote rv " +
            "LEFT JOIN rv.recipeVoteId rvid " +
            "WHERE rvid.recipe = :recipe AND rv.isUpvote =:isUpvote"
    )
    int countAmountOfVotesUsingIsUpvoteAndRecipe(Recipe recipe, boolean isUpvote);
}

package dk.minkostplan.backend.repository;

import dk.minkostplan.backend.entities.Ingredient;
import dk.minkostplan.backend.entities.Recipe;
import dk.minkostplan.backend.models.Approval;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {

    @Query("SELECT r FROM Recipe r " +
            "LEFT JOIN FETCH r.macros " +
            "LEFT JOIN FETCH r.analyzedInstructions " +
            "WHERE r.id = :id")
    Optional<Recipe> getRecipe(Long id);

    @Query("SELECT DISTINCT(i) from Ingredient i " +
            "JOIN FETCH i.measure " +
            "JOIN FETCH i.food " +
            "LEFT JOIN FETCH i.meta " +
            "where i.recipe = :recipe")
    List<Ingredient> getIngredientsUsingRecipe(Recipe recipe);

    Page<Recipe> findAllByApproval(Approval approval, Pageable pageable);

    @Modifying
    @Query("DELETE FROM Recipe r where r = :recipe")
    void deleteRecipe(Recipe recipe);

    @Modifying
    @Query("DELETE FROM Ingredient i WHERE i.recipe = :recipe")
    void deleteRecipeIngredients(Recipe recipe);

    @Modifying
    @Query("DELETE FROM RecipeInstruction i WHERE i.recipe = :recipe")
    void deleteRecipeInstructions(Recipe recipe);

    @Modifying
    @Query("DELETE FROM Macros m WHERE m.recipe = :recipe")
    void deleteRecipeMacros(Recipe recipe);

    @Modifying
    @Query("DELETE FROM RecipeVote rv WHERE rv.recipeVoteId.recipe = :recipe")
    void deleteRecipeVotes(Recipe recipe);
}

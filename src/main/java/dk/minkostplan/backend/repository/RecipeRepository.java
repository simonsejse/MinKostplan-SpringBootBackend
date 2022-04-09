package dk.minkostplan.backend.repository;

import dk.minkostplan.backend.entities.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    @Query("SELECT r FROM Recipe r " +
            "JOIN FETCH r.ingredients ig " +
            "JOIN FETCH r.macros " +
            "INNER JOIN FETCH ig.food " +
            "INNER JOIN FETCH ig.measure " +
            "LEFT JOIN FETCH ig.meta " +
            "WHERE r.id = :id")
    Optional<Recipe> getRecipeFetchIngredients(Long id);

    @Query("SELECT r FROM Recipe r " +
            "JOIN FETCH r.analyzedInstructions a " +
            "WHERE r = :recipe")
    Recipe getRecipeFetchInstructions(Recipe recipe);



    @Modifying
    @Query("DELETE FROM Recipe r where r = :recipe")
    void deleteRecipe(Recipe recipe);

    @Modifying
    @Query("DELETE FROM Ingredient i WHERE i.recipe = :recipe")
    void deleteRecipeIngredients(Recipe recipe);

    @Modifying
    @Query("DELETE FROM RecipeInstruction i WHERE i.recipe = :recipe")
    void deleteRecipeInstructions(Recipe recipe);
}

package dk.minkostplan.backend.repository;

import dk.minkostplan.backend.entities.Recipe;
import dk.minkostplan.backend.models.dtos.recipes.RecipeDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.Optional;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    @Query("SELECT new dk.minkostplan.backend.models.dtos.recipes.RecipeDTO(recipe) from Recipe recipe where recipe.id = :id")
    Optional<RecipeDTO> getRecipeDTOByRecipeId(Long id);

    @Modifying
    @Query("DELETE FROM Recipe r where r = :recipe")
    void deleteRecipe(Recipe recipe);

    @Modifying
    @Query("DELETE FROM Ingredient i WHERE i.recipe = :recipe")
    void deleteRecipeIngredients(Recipe recipe);

    @Modifying
    @Query("DELETE FROM MealInstruction i WHERE i.recipe = :recipe")
    void deleteRecipeInstructions(Recipe recipe);
}

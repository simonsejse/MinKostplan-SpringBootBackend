package dk.minkostplan.backend.repository;

import dk.minkostplan.backend.entities.Recipe;
import dk.minkostplan.backend.models.dtos.RecipeDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    @Query("SELECT new dk.minkostplan.backend.models.dtos.RecipeDTO(recipe) from Recipe recipe where recipe.recipeId = :recipeId")
    Optional<RecipeDTO> getRecipeDTOByRecipeId(Long recipeId);
}

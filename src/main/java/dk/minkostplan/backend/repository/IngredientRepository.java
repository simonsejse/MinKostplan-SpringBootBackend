package dk.minkostplan.backend.repository;

import dk.minkostplan.backend.entities.Ingredient;
import dk.minkostplan.backend.entities.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface IngredientRepository extends JpaRepository<Ingredient, Long> {
    @Query("SELECT i FROM Ingredient i WHERE i.recipe = :recipe")
    Set<Ingredient> getIngredientsOfRecipe(Recipe recipe);
    @Query("SELECT i FROM Ingredient i WHERE i.recipe IN :recipes")
    Set<Ingredient> getIngredientsOfRecipes(List<Recipe> recipes);
}

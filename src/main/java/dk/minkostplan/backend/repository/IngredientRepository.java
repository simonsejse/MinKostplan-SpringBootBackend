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
  /*
  @Query("SELECT i from Ingredient i where i.recipe = :recipe")
    Optional<Set<Ingredient>> getRecipeIngredients(Recipe recipe);
   */
}

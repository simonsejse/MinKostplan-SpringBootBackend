package dk.minkostplan.backend.repository;

import dk.minkostplan.backend.entities.Food;
import dk.minkostplan.backend.entities.Ingredient;
import dk.minkostplan.backend.interfaceprojections.FoodProjection;
import dk.minkostplan.backend.payload.response.recipes.FoodDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface FoodRepository extends JpaRepository<Food, Long> {
    @Query("SELECT f from Food f")
    List<FoodProjection> findAllFoodAsProjection();

    @Query("SELECT f FROM Food f WHERE f.ingredients = :ingredients")
    List<Food> getFoodByIngredients(Set<Ingredient> ingredients);

    Optional<FoodDTO> getFoodById(Long id);

    Boolean existsByName(String name);
}

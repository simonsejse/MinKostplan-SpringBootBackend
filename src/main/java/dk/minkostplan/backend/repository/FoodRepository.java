package dk.minkostplan.backend.repository;

import dk.minkostplan.backend.entities.Food;
import dk.minkostplan.backend.entities.Ingredient;
import dk.minkostplan.backend.models.dtos.recipes.FoodDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;

public interface FoodRepository extends JpaRepository<Food, Long> {
    @Query("SELECT new dk.minkostplan.backend.models.dtos.recipes.FoodDTO(f) from Food f")
    List<FoodDTO> findAllFoodDTOs();

    @Query("SELECT f FROM Food f WHERE f.ingredients = :ingredients")
    List<Food> getFoodByIngredients(Set<Ingredient> ingredients);

    Boolean existsByName(String name);

    Food findByName(String name);
}

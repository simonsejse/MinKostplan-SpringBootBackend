package dk.minkostplan.backend.repository;

import dk.minkostplan.backend.entities.Food;
import dk.minkostplan.backend.models.dtos.FoodDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FoodRepository extends JpaRepository<Food, Long> {
    @Query("SELECT new dk.minkostplan.backend.models.dtos.FoodDTO(f) from Food f")
    List<FoodDTO> findAllFoodDTOs();

    Food findByName(String name);
}

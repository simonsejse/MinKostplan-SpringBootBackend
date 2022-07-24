package dk.minkostplan.backend.service;

import dk.minkostplan.backend.entities.Food;
import dk.minkostplan.backend.interfaceprojections.FoodProjection;
import dk.minkostplan.backend.payload.response.recipes.FoodDTO;
import dk.minkostplan.backend.repository.FoodRepository;
import dk.minkostplan.backend.exceptions.FoodException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FoodService {
    private final FoodRepository foodRepo;

    @Autowired
    public FoodService(FoodRepository foodRepo){
        this.foodRepo = foodRepo;
    }

    public List<FoodProjection> findAllFoodAsProjections() {
        return foodRepo.findAllFoodAsProjection();
    }

    public Food getFoodById(long foodId) throws FoodException {
        return foodRepo.findById(foodId)
                .orElseThrow(
                        () -> new FoodException(HttpStatus.NOT_FOUND, String.format("Maden med id %d findes ikke!", foodId))
                );
    }

    public void createNewFood(FoodDTO foodDTO) throws FoodException {
        if (foodRepo.existsByName(foodDTO.getName())){
            throw new FoodException(HttpStatus.CONFLICT, "Mad navnet eksistere allerede i databasen!");
        }
        Food food = new Food(
                foodDTO
        );
        foodRepo.save(food);
    }

    public FoodDTO getFoodDTOById(Long id) throws FoodException {
        final FoodDTO foodById = foodRepo.getFoodById(id)
                .orElseThrow(
                        () -> new FoodException(HttpStatus.NOT_FOUND, "Kunne ikke finde den specfikke ingrediens!")
                );
        return foodById;
    }

}

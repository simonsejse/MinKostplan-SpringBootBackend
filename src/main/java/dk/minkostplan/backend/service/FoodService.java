package dk.minkostplan.backend.service;

import dk.minkostplan.backend.entities.Food;
import dk.minkostplan.backend.models.dtos.recipes.FoodDTO;
import dk.minkostplan.backend.repository.FoodRepository;
import dk.testproject.basketbackend.exceptions.FoodException;
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

    public List<FoodDTO> findAllFoodDTOs() {
        return foodRepo.findAllFoodDTOs();
    }

    public Food getFoodById(long foodId) throws FoodException {
        return foodRepo.findById(foodId)
                .orElseThrow(
                        () -> new FoodException(HttpStatus.NOT_FOUND, String.format("Maden med id %d findes ikke!", foodId))
                );
    }
}

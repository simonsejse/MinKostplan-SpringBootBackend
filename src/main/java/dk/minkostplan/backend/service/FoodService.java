package dk.minkostplan.backend.service;

import dk.minkostplan.backend.models.dtos.FoodDTO;
import dk.minkostplan.backend.repository.FoodRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
}

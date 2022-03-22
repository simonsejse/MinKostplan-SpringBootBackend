package dk.minkostplan.backend.controllers;

import dk.minkostplan.backend.models.dtos.FoodDTO;
import dk.minkostplan.backend.service.FoodService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/foods")
public class FoodController {
    private final FoodService foodService;

    public FoodController(FoodService foodService){
        this.foodService = foodService;
    }

    @GetMapping
    public ResponseEntity<List<FoodDTO>> getAllFoodsByDTOs(){
        return ResponseEntity.ok(foodService.findAllFoodDTOs());
    }

}

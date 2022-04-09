package dk.minkostplan.backend.controllers;

import dk.minkostplan.backend.exceptions.FoodException;
import dk.minkostplan.backend.models.dtos.recipes.FoodDTO;
import dk.minkostplan.backend.service.FoodService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/new")
    public ResponseEntity<String> addFood(@RequestBody FoodDTO foodDTO) throws FoodException {
        foodService.createNewFood(foodDTO);
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }


}

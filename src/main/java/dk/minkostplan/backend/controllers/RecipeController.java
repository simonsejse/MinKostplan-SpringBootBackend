package dk.minkostplan.backend.controllers;

import dk.minkostplan.backend.models.dtos.RecipeDTO;
import dk.minkostplan.backend.exceptions.RecipeException;
import dk.minkostplan.backend.service.RecipeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/recipes")
public class RecipeController {

    private final RecipeService recipeService;

    public RecipeController(RecipeService recipeService){
        this.recipeService = recipeService;
    }

    @CrossOrigin("http://localhost:3000/")
    @GetMapping(
            value = "/{id}"
    )
    public @ResponseBody ResponseEntity<RecipeDTO> getMealById(@PathVariable("id") long id) throws RecipeException {
        RecipeDTO recipeDTOById = recipeService.getRecipeDTOById(id);
        return new ResponseEntity<>(recipeDTOById, HttpStatus.OK);
    }
}

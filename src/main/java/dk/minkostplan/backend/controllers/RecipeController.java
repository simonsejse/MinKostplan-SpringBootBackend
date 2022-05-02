package dk.minkostplan.backend.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import dk.minkostplan.backend.entities.*;
import dk.minkostplan.backend.exceptions.FoodException;
import dk.minkostplan.backend.exceptions.MetaException;
import dk.minkostplan.backend.exceptions.RecipeException;
import dk.minkostplan.backend.models.MeasureType;
import dk.minkostplan.backend.models.RecipeType;
import dk.minkostplan.backend.models.dtos.recipes.RecipeDTO;
import dk.minkostplan.backend.payload.request.recipe.IngredientCreateRequest;
import dk.minkostplan.backend.payload.request.recipe.MeasureCreateRequest;
import dk.minkostplan.backend.payload.request.recipe.RecipeCreateRequest;
import dk.minkostplan.backend.service.FoodService;
import dk.minkostplan.backend.service.MetaService;
import dk.minkostplan.backend.service.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.*;
import java.util.stream.Collectors;

@Validated
@RestController
@RequestMapping("api/recipes")
public class RecipeController {

    private final RecipeService recipeService;
    private final ObjectMapper objectMapper;

    private final MetaService metaService;
    private final FoodService foodService;

    @Autowired
    public RecipeController(RecipeService recipeService, MetaService metaService, ObjectMapper objectMapper, FoodService foodService){
        this.recipeService = recipeService;
        this.metaService = metaService;
        this.objectMapper = objectMapper;
        this.foodService = foodService;
    }

    @CrossOrigin("http://localhost:3000/")
    @GetMapping("/categories")
    public @ResponseBody ResponseEntity<List<String>> getRecipeCategories(){
        List<String> recipeCategories = Arrays.stream(RecipeType.values())
                .map(RecipeType::name)
                .collect(Collectors.toList());
        return ResponseEntity.ok(recipeCategories);
    }


    @CrossOrigin("http://localhost:3000/")
    @GetMapping("/{id}")
    public @ResponseBody ResponseEntity<RecipeDTO> getRecipeById(
            @PathVariable("id") long id,
            @RequestParam(required = false) @Min(value=50, message = "Kalorier kan ikke være under 50!") Float calories
    ) throws RecipeException {
        RecipeDTO recipeDTOById = recipeService.getRecipeDTOById(id, calories);
        return new ResponseEntity<>(recipeDTOById, HttpStatus.OK);
    }

    @GetMapping("/random")
    public @ResponseBody ResponseEntity<List<RecipeDTO>> getRandomRecipes(@RequestParam(name="amount") Optional<Integer> amount){
        return new ResponseEntity<>(recipeService.getRandomRecipes(amount.orElse(1)), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Map<String, String>> deleteOldRecipe(@PathVariable("id") Long recipeId) throws RecipeException{
        recipeService.deleteRecipeById(recipeId);
        Map<String, String> message = Map.of("message", "opskrift slettet", "recipe_id", String.valueOf(recipeId));
        return new ResponseEntity<>(message, HttpStatus.OK);
    }


    @PostMapping("/new")
    public @ResponseBody ResponseEntity<Object> createNewRecipe(
            @Valid @RequestBody RecipeCreateRequest recipe
    ) throws RecipeException, MetaException, FoodException {
        float calories = 0f, protein = 0f, fat = 0f, carbs = 0f;

        Recipe newRecipe = new Recipe.Builder()
                .vegetarian(recipe.getVegetarian())
                .vegan(recipe.getVegan())
                .type(RecipeType.valueOf(recipe.getType()))
                .glutenFree(recipe.getGlutenFree())
                .dairyFree(recipe.getDairyFree())
                .veryHealthy(recipe.getVeryHealthy())
                .cheap(recipe.getCheap())
                .veryPopular(recipe.getVeryPopular())
                .sustainable(recipe.getSustainable())
                .pricePerServing(recipe.getPricePerServing())
                .name(recipe.getName())
                .readyInMinutes(recipe.getReadyInMinutes())
                .instructions(recipe.getInstructions())
                .image(recipe.getImage())
                .build();

        for(IngredientCreateRequest ingredient : recipe.getIngredients()){
            Food food = foodService.getFoodById(ingredient.getFoodId());

            MeasureCreateRequest measures = ingredient.getMeasures();
            MeasureType measureType = measures.getType();
            float amountInGrams = measures.getAmountInGrams();
            float amountOfType = measures.getAmountOfType();

            Measurement measurement = Measurement.builder()
                    .type(measureType)
                    .amountInGrams(amountInGrams)
                    .amountOfType(amountOfType)
                    .build();

            calories += amountInGrams * (food.getKcal() / 100);
            protein += amountInGrams * (food.getProtein() / 100);
            fat += amountInGrams * (food.getFat() / 100);
            carbs += amountInGrams * (food.getCarbs() / 100);

            Set<Meta> metas = new HashSet<>();
            for(String metaName : ingredient.getMeta()){
                Meta meta = metaService.getMeta(metaName);
                metas.add(meta);
            }

            Ingredient recipe_ingredient = Ingredient.builder()
                    .food(food)
                    .recipe(newRecipe)
                    .amount(ingredient.getAmount())
                    .instruction(ingredient.getInstruction())
                    .measure(measurement)
                    .meta(metas)
                    .build();

            newRecipe.addIngredient(recipe_ingredient);
        }
        recipe.getAnalyzedInstructions().stream().map(RecipeInstruction::new).forEach(newRecipe::addInstruction);


        Macros macros = new Macros(calories, protein, fat, carbs);
        newRecipe.setMacros(macros);

        recipeService.createRecipe(newRecipe);

        Map<String, String> message = Map.of("message", "opskrift oprettet!", "recipe_id", String.valueOf(newRecipe.getId()));
        return new ResponseEntity<>(message, HttpStatus.OK);

    }
}

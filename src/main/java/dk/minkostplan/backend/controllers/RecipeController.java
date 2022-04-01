package dk.minkostplan.backend.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dk.minkostplan.backend.entities.*;
import dk.minkostplan.backend.exceptions.MetaException;
import dk.minkostplan.backend.models.dtos.recipes.RecipeDTO;
import dk.minkostplan.backend.exceptions.RecipeException;
import dk.minkostplan.backend.service.FoodService;
import dk.minkostplan.backend.service.MetaService;
import dk.minkostplan.backend.service.RecipeService;
import dk.testproject.basketbackend.exceptions.FoodException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

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

    @GetMapping("/{id}")
    public @ResponseBody ResponseEntity<RecipeDTO> getRecipeById(@PathVariable("id") long id) throws RecipeException {
        RecipeDTO recipeDTOById = recipeService.getRecipeDTOById(id);
        return new ResponseEntity<>(recipeDTOById, HttpStatus.OK);
    }

    @GetMapping("/random")
    public @ResponseBody ResponseEntity<List<RecipeDTO>> getRandomRecipes(@RequestParam(name="amount") Optional<Integer> amount){
        return new ResponseEntity<>(recipeService.getRandomRecipes(amount.orElse(1)), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteOldRecipe(@PathVariable("id") Long recipeId) throws RecipeException{
        recipeService.deleteRecipeById(recipeId);
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }

    @PostMapping("/new")
    public ResponseEntity<String> createNewRecipe(@RequestBody Map<String, Object> test) throws RecipeException, JsonProcessingException, MetaException, FoodException {
        JsonNode root = objectMapper.readTree(objectMapper.writeValueAsString(test));

        Recipe newRecipe = new Recipe.Builder()
                .vegetarian(root.get("vegetarian").asBoolean())
                .vegan(root.get("vegan").asBoolean())
                .type(root.get("type").asText())
                .glutenFree(root.get("glutenFree").asBoolean())
                .dairyFree(root.get("dairyFree").asBoolean())
                .veryHealthy(root.get("veryHealthy").asBoolean())
                .cheap(root.get("cheap").asBoolean())
                .veryPopular(root.get("veryPopular").asBoolean())
                .sustainable(root.get("sustainable").asBoolean())
                .pricePerServing(root.get("pricePerServing").asDouble())
                .name(root.get("name").asText())
                .readyInMinutes(root.get("readyInMinutes").asInt())
                .serving(root.get("servings").asInt())
                .instructions(root.get("instructions").asText())
                .build();

        JsonNode instructions = root.get("analyzedInstructions");

        for(JsonNode instruction : instructions){
            int number = instruction.get("number").asInt();
            String step = instruction.get("step").asText();
            newRecipe.addInstruction(new RecipeInstruction(number, step), number);
        }

        JsonNode ingredients = root.get("ingredients");

        for(JsonNode ingredient : ingredients){
            String original = ingredient.get("original").asText();
            double amount = ingredient.get("amount").asDouble();
            String unit = ingredient.get("unit").asText();
            JsonNode metasNode = ingredient.get("meta");

            Set<Meta> metas = new HashSet<>();
            for(JsonNode metaNode : metasNode){
                Meta meta = metaService.getMeta(metaNode.asText());
                metas.add(meta);
            }

            Food food = foodService.getFoodById(ingredient.get("fk_food_id").asInt());

            Ingredient recipe_ingredient = new Ingredient(
                    food,
                    newRecipe,
                    (float) amount,
                    original,
                    unit,
                    metas
            );

            newRecipe.addIngredient(recipe_ingredient);
        }

        recipeService.createRecipe(newRecipe);

        return new ResponseEntity<>("OK", HttpStatus.OK);
    }
}

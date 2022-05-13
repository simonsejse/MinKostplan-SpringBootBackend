package dk.minkostplan.backend.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import dk.minkostplan.backend.entities.*;
import dk.minkostplan.backend.exceptions.FoodException;
import dk.minkostplan.backend.exceptions.MetaException;
import dk.minkostplan.backend.exceptions.RecipeException;
import dk.minkostplan.backend.models.MeasureType;
import dk.minkostplan.backend.models.RecipeApproval;
import dk.minkostplan.backend.models.RecipeType;
import dk.minkostplan.backend.models.dtos.recipes.RecipeDTO;
import dk.minkostplan.backend.payload.request.RecipeViewList;
import dk.minkostplan.backend.payload.request.recipe.IngredientCreateRequest;
import dk.minkostplan.backend.payload.request.recipe.MeasureCreateRequest;
import dk.minkostplan.backend.payload.request.recipe.RecipeCreateRequest;
import dk.minkostplan.backend.service.FoodService;
import dk.minkostplan.backend.service.MetaService;
import dk.minkostplan.backend.service.RecipeService;
import dk.minkostplan.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
    private final UserService userService;

    @Autowired
    public RecipeController(@Qualifier("recipeService") RecipeService recipeService,
                            @Qualifier("metaService") MetaService metaService,
                            @Qualifier("objectMapper") ObjectMapper objectMapper,
                            @Qualifier("foodService") FoodService foodService,
                            @Qualifier("userService") UserService userService
    ) {
        this.recipeService = recipeService;
        this.metaService = metaService;
        this.objectMapper = objectMapper;
        this.foodService = foodService;
        this.userService = userService;
    }

    @CrossOrigin("http://localhost:3000/")
    @GetMapping("/categories")
    public @ResponseBody
    ResponseEntity<List<String>> getRecipeCategories() {
        List<String> recipeCategories = Arrays.stream(RecipeType.values())
                .map(RecipeType::name)
                .collect(Collectors.toList());
        return ResponseEntity.ok(recipeCategories);
    }



    @CrossOrigin("http://localhost:3000/")
    @GetMapping("/{id}")
    public @ResponseBody
    ResponseEntity<RecipeDTO> getRecipeById(
            @PathVariable("id") long id,
            @RequestParam(required = false) @Min(value = 50, message = "Kalorier kan ikke være under 50!") Float calories
    ) throws RecipeException {
        RecipeDTO recipeDTOById = recipeService.getRecipeDTOById(id, calories);
        return new ResponseEntity<>(recipeDTOById, HttpStatus.OK);
    }

    @GetMapping("/awaiting-approval")
    public Page<RecipeViewList> findAwaitedRecipes(Pageable pageable){
        return recipeService.findAllByApproval(RecipeApproval.AWAITING_CONFIRMATION, pageable);
    }

    @GetMapping("/random")
    public @ResponseBody
    ResponseEntity<List<RecipeDTO>> getRandomRecipes(@RequestParam(name = "amount") Optional<Integer> amount) {

        return new ResponseEntity<>(recipeService.getRandomRecipes(amount.orElse(1)), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Map<String, String>> deleteOldRecipe(@PathVariable("id") Long recipeId) throws RecipeException {
        recipeService.deleteRecipeById(recipeId);
        Map<String, String> message = Map.of("message", "opskrift slettet", "recipe_id", String.valueOf(recipeId));
        return new ResponseEntity<>(message, HttpStatus.OK);
    }


    @PostMapping("/new")
    public @ResponseBody
    ResponseEntity<Object> createNewRecipe(
            @Valid @RequestBody RecipeCreateRequest recipe,
            Authentication authentication
    ) throws RecipeException, MetaException, FoodException, UsernameNotFoundException {
        float calories = 0f, protein = 0f, fat = 0f, carbs = 0f;
        if (authentication.getName() == null)
            throw new RecipeException("Kunne ikke finde brugeren, du er umiddelbart ikke logget ind!", HttpStatus.UNAUTHORIZED);

        //CHANGE EVERYTHING IN HERE TO A SERVICE WITH TRANSACTIONMAL
        //CHANGE EVERYTHING IN HERE TO A SERVICE WITH TRANSACTIONMAL
        //CHANGE EVERYTHING IN HERE TO A SERVICE WITH TRANSACTIONMAL

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
                .description(recipe.getDescription())
                .readyInMinutes(recipe.getReadyInMinutes())
                .instructions(recipe.getInstructions())
                .image(recipe.getImage())
                .build();

        for (IngredientCreateRequest ingredient : recipe.getIngredients()) {
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
            for (String metaName : ingredient.getMeta()) {
                Meta meta = metaService.getMeta(metaName);
                metas.add(meta);
            }

            Ingredient recipe_ingredient = Ingredient.builder()
                    .food(food)
                    .recipe(newRecipe)
                    .amount(ingredient.getAmount())
                    .measure(measurement)
                    .meta(metas)
                    .build();

            newRecipe.addIngredient(recipe_ingredient);
        }

        recipe.getAnalyzedInstructions().stream().map(RecipeInstruction::new).forEach(newRecipe::addInstruction);

        Macros macros = new Macros(calories, protein, fat, carbs);
        newRecipe.setMacros(macros);


        String username = authentication.getName();
        User userByUsername = userService.getUserByUsername(username);

        newRecipe.setCreatedBy(userByUsername);
        recipeService.createRecipe(newRecipe);

        Map<String, String> message = Map.of("message", "opskrift oprettet!", "recipe_id", String.valueOf(newRecipe.getId()));
        return new ResponseEntity<>(message, HttpStatus.CREATED);

    }
}

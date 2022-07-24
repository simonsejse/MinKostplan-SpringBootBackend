package dk.minkostplan.backend.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.istack.Nullable;
import dk.minkostplan.backend.entities.*;
import dk.minkostplan.backend.exceptions.FoodException;
import dk.minkostplan.backend.exceptions.MetaException;
import dk.minkostplan.backend.exceptions.RecipeException;
import dk.minkostplan.backend.interfaceprojections.DisplayRecipeProjection;
import dk.minkostplan.backend.models.MeasureType;
import dk.minkostplan.backend.models.Approval;
import dk.minkostplan.backend.models.RecipeType;
import dk.minkostplan.backend.payload.response.recipes.RecipeDTO;
import dk.minkostplan.backend.payload.response.RecipesPendingDTO;
import dk.minkostplan.backend.payload.request.recipe.IngredientCreateRequest;
import dk.minkostplan.backend.payload.request.recipe.MeasureCreateRequest;
import dk.minkostplan.backend.payload.request.recipe.RecipeCreateRequest;
import dk.minkostplan.backend.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
@RestController(value= "api/recipes")
@RequestMapping("api/recipes")
public class RecipeController {

    private final RecipeService recipeService;
    private final ObjectMapper objectMapper;

    private final MetaService metaService;
    private final FoodService foodService;
    private final UserService userService;
    private final IngredientService ingredientService;
    private final RecipeVoteService recipeVoteService;

    @Autowired
    public RecipeController(@Qualifier("recipeService") RecipeService recipeService, @Qualifier("metaService") MetaService metaService, @Qualifier("objectMapper") ObjectMapper objectMapper, @Qualifier("foodService") FoodService foodService, @Qualifier("userService") UserService userService, IngredientService ingredientService, @Qualifier("recipeVoteService") RecipeVoteService recipeVoteService) {
        this.recipeService = recipeService;
        this.metaService = metaService;
        this.objectMapper = objectMapper;
        this.foodService = foodService;
        this.userService = userService;
        this.ingredientService = ingredientService;
        this.recipeVoteService = recipeVoteService;
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
    @GetMapping("/vote/{id}")
    public ResponseEntity<String> createNewRecipeVoteByRecipeAndUser(
            @PathVariable("id") long recipeId,
            @RequestParam(value = "isUpvote", required = true) boolean isUpvote,
            Authentication authentication
    ) throws RecipeException, UsernameNotFoundException {
        //TODO: Change exception thrown later!
        if (authentication == null)
            throw new IllegalStateException("Du er ikke logget ind!");

        User user = userService.getUserByUsername(authentication.getName());
        Recipe recipe = recipeService.getRecipeById(recipeId);
        recipeVoteService.createNewRecipeVoteByRecipeAndUser(recipe, user, isUpvote);
        return ResponseEntity.ok("OK");
    }

    @CrossOrigin("http://localhost:3000/")
    @GetMapping("/{id}")
    public @ResponseBody
    ResponseEntity<RecipeDTO> getRecipeById(
            @PathVariable("id") long id,
            @RequestParam(required = false, name = "calories") @Min(value = 50, message = "Kalorier kan ikke være under 50!") Integer calories
    ) throws RecipeException {
        RecipeDTO recipeDTOById = recipeService.getRecipeDTOById(id, calories);
        return new ResponseEntity<>(recipeDTOById, HttpStatus.OK);
    }

    @GetMapping("/show")
    public Page<DisplayRecipeProjection.DTO> getPageOfRecipeDisplays(
            Pageable pageable,
            @RequestParam("searchByName")
            @Nullable
            String searchByName,
            @RequestParam(value = "caloriesWanted", required = false)
            @Min(value = 50, message = "Du kan ikke vælge mindre end 50 kalorier til en opskrift!")
            @Nullable
            Integer caloriesWanted
    ){
        try {
            Thread.sleep(1500); //Faking network delay just to see if loading work
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return recipeService.getPageOfRecipeDisplays(Approval.ACCEPTED, pageable, searchByName, caloriesWanted);
    }

    @GetMapping("/awaiting-approval")
    public Page<RecipesPendingDTO> findAwaitedRecipes(Pageable pageable){
        return recipeService.findAllByApproval(Approval.PENDING, pageable);
    }


    @GetMapping("/random")
    public @ResponseBody
    ResponseEntity<List<RecipeDTO>> getRandomRecipes(@RequestParam(name = "amount") Optional<Integer> amount) {
        return new ResponseEntity<>(recipeService.getRandomRecipes(amount.orElse(1)), HttpStatus.OK);
    }

    @PostMapping("/confirm/{id}")
    public ResponseEntity<String> confirmRecipeById(@PathVariable("id") long recipeId){
        recipeService.acceptRecipeById(recipeId);
        return new ResponseEntity<>("Du har accepteret opskrift #" + recipeId + " den kan nu findes i systemet af andre brugere!", HttpStatus.OK);
    }



    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteOldRecipe(@PathVariable("id") Long recipeId) throws RecipeException {
        recipeService.deleteRecipeById(recipeId);
        return new ResponseEntity<>(String.format("Opskrift #%d blev slettet fra databasen!", recipeId), HttpStatus.OK);
    }

    @PostMapping("/new")
    public @ResponseBody
    ResponseEntity<Object> createNewRecipe(
            @Valid @RequestBody RecipeCreateRequest recipe,
            Authentication authentication
    ) throws RecipeException, MetaException, FoodException, UsernameNotFoundException {
        int calories = 0, protein = 0, fat = 0, carbs = 0;
        if (authentication == null)
            throw new RecipeException("Kunne ikke finde brugeren, du er umiddelbart ikke logget ind!", HttpStatus.UNAUTHORIZED);

        //CHANGE EVERYTHING IN HERE TO A SERVICE WITH TRANSACTIONMAL
        //CHANGE EVERYTHING IN HERE TO A SERVICE WITH TRANSACTIONMAL
        //CHANGE EVERYTHING IN HERE TO A SERVICE WITH TRANSACTIONMAL



        Recipe newRecipe = new Recipe.Builder()
                .vegetarian(recipe.getVegetarian())
                .vegan(recipe.getVegan())
                .type(recipe.getType())
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

            calories = (int) (calories + amountInGrams * (food.getKcal() / 100));
            protein += amountInGrams * (food.getProtein() / 100);
            fat += amountInGrams * (food.getFat() / 100);
            carbs += amountInGrams * (food.getCarbs() / 100);

           /*Set<Meta> recipeMeta = new HashSet<>();
            for (String metaName : ingredient.getMeta()) {
                Meta meta = metaService.getMeta(metaName);
                metas.add(meta);
            }*/

            Ingredient recipeIngredient = Ingredient.builder()
                    .food(food)
                    .recipe(newRecipe)
                    .amount(ingredient.getAmount())
                    .measure(measurement)
                    .build();

            ingredientService.saveIngredient(recipeIngredient);
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

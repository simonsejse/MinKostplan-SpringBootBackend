package dk.minkostplan.backend.service;

import dk.minkostplan.backend.entities.Ingredient;
import dk.minkostplan.backend.entities.Recipe;
import dk.minkostplan.backend.exceptions.RecipeException;
import dk.minkostplan.backend.interfaceprojections.DisplayRecipeProjection;
import dk.minkostplan.backend.models.Approval;
import dk.minkostplan.backend.payload.response.recipes.*;
import dk.minkostplan.backend.payload.response.RecipesPendingDTO;
import dk.minkostplan.backend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.constraints.Min;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Service
public class RecipeService {


    private final RecipeRepository recipeRepository;
    private final IngredientRepository ingredientRepository;
    private final MetaRepository metaRepository;
    private final FoodRepository foodRepository;
    private final RecipeVoteRepository recipeVoteRepository;

    @Autowired
    public RecipeService(RecipeRepository recipeRepository, IngredientRepository ingredientRepository, MetaRepository metaRepository, FoodRepository foodRepository, RecipeVoteRepository recipeVoteRepository) {
        this.recipeRepository = recipeRepository;
        this.ingredientRepository = ingredientRepository;
        this.metaRepository = metaRepository;
        this.foodRepository = foodRepository;
        this.recipeVoteRepository = recipeVoteRepository;
    }

    public Recipe getRecipeById(long recipeId) throws RecipeException {
        return recipeRepository.findById(recipeId).orElseThrow(new Supplier<RecipeException>(){
            @Override
            public RecipeException get() {
                return new RecipeException(
                        String.format("Der findes ikke et måltid med ID %d!", recipeId), HttpStatus.NOT_FOUND
                );
            }
        });
    }

    @Transactional
    public RecipeDTO getRecipeDTOById(long recipeId,
                                      @Min(value = 10, message = "Kalorier kan ikke være under 10!") Integer calories
    ) throws RecipeException {
        Optional<Recipe> recipeFetchIngredients = recipeRepository.getRecipe(recipeId);

        Recipe recipe = recipeFetchIngredients.orElseThrow(new Supplier<RecipeException>() {
           @Override
           public RecipeException get() {
               return new RecipeException(
                       String.format("Der findes ikke et måltid med ID %d!", recipeId), HttpStatus.NOT_FOUND
               );
           }
       }
        );

        RecipeDTO recipeDTO = new RecipeDTO(recipe);

        recipeDTO.setMacros(new MacroDTO(recipe.getMacros(), Optional.ofNullable(calories)));

        MacroDTO macros = recipeDTO.getMacros();
        float ratio = macros.getWanted().getCalories() / macros.getNormal().getCalories();

        List<Ingredient> ingredients = recipeRepository.getIngredientsUsingRecipe(recipe);

        List<IngredientDTO> ingredientDTOList = ingredients
                .stream()
                .map(ingredient -> {
                    IngredientDTO ingredientDTO = new IngredientDTO(ingredient);
                    ingredientDTO.setAmount(ingredientDTO.getAmount() * ratio);
                    MeasurementsDTO measurementsDTO = ingredientDTO.getMeasurementsDTO();
                    measurementsDTO.setAmountInGrams(measurementsDTO.getAmountInGrams() * ratio);
                    measurementsDTO.setAmountOfType(measurementsDTO.getAmountOfType() * ratio);
                    return ingredientDTO;
                })
                .collect(Collectors.toList());

        List<AnalysedInstructionDTO> analyzedInstructions = recipe.getAnalyzedInstructions()
                .stream()
                .map(AnalysedInstructionDTO::new)
                .collect(Collectors.toList());

        recipeDTO.setIngredients(ingredientDTOList);
        recipeDTO.setAnalyzedInstructions(analyzedInstructions);

        return recipeDTO;
    }


    public List<RecipeDTO> getRandomRecipes(Integer amount) {
        return List.of();
    }

    @Transactional
    public void createRecipe(Recipe newRecipe) {
        this.recipeRepository.save(newRecipe);
    }

    @Transactional
    public void acceptRecipeById(long recipeId) {
        final Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(
                    () -> new RecipeException(String.format("Opskrift med id %d findes ikke!", recipeId), HttpStatus.NOT_FOUND)
                );
        recipe.setApproval(Approval.ACCEPTED);
    }

    @Transactional
    public void deleteRecipeById(Long recipeId) throws RecipeException {
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(
                    () -> new RecipeException(String.format("Opskrift med id %d findes ikke!", recipeId), HttpStatus.NOT_FOUND)
                );

        recipeRepository.deleteRecipeIngredients(recipe);
        recipeRepository.deleteRecipeInstructions(recipe);
        recipeRepository.deleteRecipeMacros(recipe);
        recipeRepository.deleteRecipeVotes(recipe);
        recipeRepository.deleteRecipe(recipe);

    }

    /**
     *
     * TODO: Could be a lot more efficient:
     */
    @Transactional
    public Page<RecipesPendingDTO> findAllByApproval(Approval approval, Pageable pageable) {
        Page<Recipe> recipes = recipeRepository.findAllByApproval(approval, pageable);
        return recipes.map(recipe -> {
            int upvotes = recipeVoteRepository.countAmountOfVotesUsingIsUpvoteAndRecipe(recipe, true);
            int downvotes = recipeVoteRepository.countAmountOfVotesUsingIsUpvoteAndRecipe(recipe, false);
            return new RecipesPendingDTO(recipe, upvotes, downvotes);
        });
    }

    public Page<DisplayRecipeProjection.DTO> getPageOfRecipeDisplays(Approval approval, Pageable pageable, String searchByName, Integer caloriesWanted) {
        final Page<DisplayRecipeProjection> pageOfRecipeDisplay = recipeRepository.getPageOfRecipeDisplay(approval, pageable, searchByName);
        final Page<DisplayRecipeProjection.DTO> displayRecipeDTO =
                caloriesWanted == null
                    ? pageOfRecipeDisplay.map(DisplayRecipeProjection.DTO::new)
                    : pageOfRecipeDisplay.map(displayRecipeProjection -> new DisplayRecipeProjection.DTO(displayRecipeProjection, caloriesWanted));
        return displayRecipeDTO;
    }


}

package dk.minkostplan.backend.service;

import dk.minkostplan.backend.entities.Meta;
import dk.minkostplan.backend.entities.Recipe;
import dk.minkostplan.backend.exceptions.RecipeException;
import dk.minkostplan.backend.models.dtos.recipes.*;
import dk.minkostplan.backend.payload.request.NutritionalValuesRequest;
import dk.minkostplan.backend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class RecipeService {


    private final RecipeRepository recipeRepository;
    private final IngredientRepository ingredientRepository;
    private final MetaRepository metaRepository;
    private final FoodRepository foodRepository;

    @Autowired
    public RecipeService(RecipeRepository recipeRepository, IngredientRepository ingredientRepository, MetaRepository metaRepository, FoodRepository foodRepository) {
        this.recipeRepository = recipeRepository;
        this.ingredientRepository = ingredientRepository;
        this.metaRepository = metaRepository;
        this.foodRepository = foodRepository;
    }

    @Transactional
    public RecipeDTO getRecipeDTOById(long id, NutritionalValuesRequest nutritionalValuesRequest) throws RecipeException {
        Recipe recipe = recipeRepository.getRecipeFetchIngredients(id).orElseThrow(() ->
                new RecipeException(String.format("Der findes ikke et måltid med ID %d!", id), HttpStatus.NOT_FOUND)
        );

        recipe = recipeRepository.getRecipeFetchInstructions(recipe);

        RecipeDTO recipeDTO = new RecipeDTO(recipe);

        recipeDTO.setMacros(new MacroDTO(recipe.getMacros(), nutritionalValuesRequest));

        MacroDTO macros = recipeDTO.getMacros();
        float ratio = macros.getWanted().getCalories() / macros.getNormal().getCalories();

        List<IngredientDTO> ingredientDTOList = recipe.getIngredients()
                .stream()
                .map(ingredient -> {
                    IngredientDTO ingredientDTO = new IngredientDTO(ingredient);
                    ingredientDTO.setAmount(ingredientDTO.getAmount() * ratio);
                    MeasurementsDTO measurementsDTO = ingredientDTO.getMeasurementsDTO();
                    measurementsDTO.setAmountInGrams(measurementsDTO.getAmountInGrams() * ratio);
                    measurementsDTO.setAmountOfType(measurementsDTO.getAmountOfType() * ratio);
                    Set<String> metas = ingredient.getMeta().stream().map(Meta::getMeta).collect(Collectors.toSet());
                    ingredientDTO.setMetas(metas);
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
    public void deleteRecipeById(Long recipeId) throws RecipeException {
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new RecipeException(String.format("Opskrift med id %d findes ikke!", recipeId), HttpStatus.NOT_FOUND));

        recipeRepository.deleteRecipeIngredients(recipe);
        recipeRepository.deleteRecipeInstructions(recipe);
        recipeRepository.deleteRecipe(recipe);

    }
}

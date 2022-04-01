package dk.minkostplan.backend.service;

import dk.minkostplan.backend.entities.Recipe;
import dk.minkostplan.backend.exceptions.RecipeException;
import dk.minkostplan.backend.models.dtos.recipes.RecipeDTO;
import dk.minkostplan.backend.repository.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class RecipeService {
    private final RecipeRepository recipeRepository;

    @Autowired
    public RecipeService(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    public RecipeDTO getRecipeDTOById(long id) throws RecipeException {
        return recipeRepository.getRecipeDTOByRecipeId(id).orElseThrow(() ->
                new RecipeException(String.format("Der findes ikke et måltid med ID %d!", id), HttpStatus.NOT_FOUND)
        );
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

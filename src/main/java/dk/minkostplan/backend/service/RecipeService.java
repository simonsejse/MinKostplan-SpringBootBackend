package dk.minkostplan.backend.service;

import dk.minkostplan.backend.exceptions.RecipeException;
import dk.minkostplan.backend.models.dtos.RecipeDTO;
import dk.minkostplan.backend.repository.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

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


}

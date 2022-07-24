package dk.minkostplan.backend.service;

import dk.minkostplan.backend.entities.Ingredient;
import dk.minkostplan.backend.repository.IngredientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IngredientService {
    private final IngredientRepository repository;

    @Autowired
    public IngredientService(IngredientRepository repository){
        this.repository = repository;
    }

    public void saveIngredient(Ingredient ingredient){
        this.repository.save(ingredient);
    }
}

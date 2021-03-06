package dk.minkostplan.backend.repository;

import dk.minkostplan.backend.entities.Ingredient;
import dk.minkostplan.backend.entities.Recipe;
import dk.minkostplan.backend.interfaceprojections.DisplayRecipeProjection;
import dk.minkostplan.backend.models.Approval;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.persistence.Transient;
import java.util.List;
import java.util.Optional;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {

    @Query("SELECT r FROM Recipe r " +
            "LEFT JOIN FETCH r.macros " +
            "LEFT JOIN FETCH r.analyzedInstructions " +
            "WHERE r.id = :id")
    Optional<Recipe> getRecipe(Long id);

    @Query("SELECT DISTINCT(i) from Ingredient i " +
            "JOIN FETCH i.measure " +
            "JOIN FETCH i.food " +
            "where i.recipe = :recipe")
    List<Ingredient> getIngredientsUsingRecipe(Recipe recipe);

    Page<Recipe> findAllByApproval(Approval approval, Pageable pageable);


    @Query("SELECT " +
            "r.id AS id, " +
            "r.name as name, " +
            "r.description as description, " +
            "r.macros.calories AS calories, " +
            "r.macros.protein as protein, " +
            "r.macros.fat as fat, " +
            "r.macros.carbs as carbs, " +
            "r.pricePerServing as pricePerServing, " +
            "r.readyInMinutes as readyInMinutes, " +
            "r.image as image, " +
            "r.createdBy.username as createdBy " +
            "from Recipe r " +
            "WHERE r.approval =:approval " +
            "and ( COALESCE(null, :recipeName) is null OR r.name LIKE CONCAT('%', :recipeName, '%') )")
    Page<DisplayRecipeProjection> getPageOfRecipeDisplay(Approval approval, Pageable pageable, String recipeName);

    @Modifying
    @Query("DELETE FROM Recipe r where r = :recipe")
    void deleteRecipe(Recipe recipe);

    @Modifying
    @Query("DELETE FROM Ingredient i WHERE i.recipe = :recipe")
    void deleteRecipeIngredients(Recipe recipe);

    @Modifying
    @Query("DELETE FROM RecipeInstruction i WHERE i.recipe = :recipe")
    void deleteRecipeInstructions(Recipe recipe);

    @Modifying
    @Query("DELETE FROM Macros m WHERE m.recipe = :recipe")
    void deleteRecipeMacros(Recipe recipe);

    @Modifying
    @Query("DELETE FROM RecipeVote rv WHERE rv.recipeVoteId.recipe = :recipe")
    void deleteRecipeVotes(Recipe recipe);
}

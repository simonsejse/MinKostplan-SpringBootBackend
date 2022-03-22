package dk.minkostplan.backend.entities;


import dk.minkostplan.backend.models.RecipeType;
import lombok.Getter;
import org.hibernate.annotations.SortNatural;

import javax.persistence.*;
import java.util.*;

//Static meals in db
@Entity(name="Recipe")
@Table(name="recipe")
@Getter
public class Recipe {
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "recipe_seq_gen"
    )
    @SequenceGenerator(
            name="recipe_seq_gen",
            allocationSize = 1
    )
    @Column(name="recipe_id", unique = true)
    @Id
    private Long recipeId;

    @Column(name="recipe_name")
    private String name;

    @Enumerated(value=EnumType.STRING)
    @Column(name="recipe_type")
    private RecipeType type;

    @Column(name="is_vegetarian", nullable = false)
    private Boolean vegetarian;

    @Column(name="is_vegan", nullable = false)
    private Boolean vegan;

    @Column(name="is_gluten_free", nullable = false)
    private Boolean glutenFree;

    @Column(name="is_dairy_free")
    private Boolean dairyFree;

    @Column(name="is_very_healthy")
    private Boolean veryHealthy;

    @Column(name="is_cheap")
    private Boolean cheap;

    @Column(name="is_veryPopular")
    private Boolean veryPopular;

    @Column(name="is_sustainable")
    private Boolean sustainable;

    @Column(name="pricePerServing")
    private String pricePerServing;

    /* One Meal to Many Food Attributes */
    @OneToMany(
            fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            },
            mappedBy= "recipe"
    )
    private final List<FoodAttribute> ingredients = new ArrayList<>();

    @OneToMany(
        fetch = FetchType.LAZY,
        cascade = {CascadeType.ALL},
        mappedBy = "recipe"
    )
    @SortNatural
    private final List<MealInstruction> instructions = new LinkedList<>();

    /* Many Meals to Many "Various" DietPlans
    * I.e. One "Meal" can be in various DietPlans */
    @ManyToMany(
            fetch = FetchType.LAZY,
            cascade = {CascadeType.MERGE,CascadeType.PERSIST}
    )
    private final Set<DietPlan> dietPlans = new HashSet<>();

    public Recipe(String name, RecipeType type) {
        this.name = name;
        this.type = type;
    }

    public Recipe() { }

    /**
     * Adding ingredients
     */

    public Recipe addIngredient(FoodAttribute foodAttr){
        ingredients.add(foodAttr);
        foodAttr.setRecipe(this);
        return this;
    }

    public Recipe removeIngredient(FoodAttribute foodAttr){
        ingredients.remove(foodAttr);
        foodAttr.setRecipe(null);
        return this;
    }

    /**
     * Adding instructions
     */

    public Long instructionsSize(){
        return (long) this.instructions.size();
    }

    public Recipe addInstruction(MealInstruction mealInstruction){
        mealInstruction.setMeal(this);
        this.instructions.add(mealInstruction);
        return this;
    }

    public Recipe removeInstruction(MealInstruction mealInstruction){
        this.instructions.remove(mealInstruction);
        mealInstruction.setMeal(null);
        return this;
    }

    /**
     * The Meal object uses the entity identifier for equality since it lacks a unique business key.
     * We cannot hash the meal_id since when the object is being attached and persisted (states) it will be located
     * in a different hashed bucket
     * We cannot use normal hashCode and equals either since

     * "Vlad - The original entity is not equal with the one returned
     * by the merge method because two distinct Object(s) do not share the same reference."
     *
     * https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
     *
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Recipe)) return false;
        Recipe recipe = (Recipe) o;
        return this.recipeId != null && this.recipeId.equals(recipe.getRecipeId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    public Long getRecipeId() {
        return recipeId;
    }
}

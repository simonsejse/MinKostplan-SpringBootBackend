package dk.minkostplan.backend.entities;


import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
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
    @Column(name="id", unique = true)
    @Id
    private Long id;

    @Column(name="recipe_name")
    private String name;

    @Column(name="recipe_type")
    private String type;

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
    private Double pricePerServing;

    @Column(name="instructionsHtml")
    private String instructions;

    @Column(name="serving")
    private Integer serving;

    @Column(name="readyInMinutes")
    private Integer readyInMinutes;

    /* One Recipe to Many Food Attributes */
    @OneToMany(
            fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            },
            mappedBy= "recipe"
    )
    private final List<Ingredient> ingredients = new ArrayList<>();

    @OneToMany(
        fetch = FetchType.LAZY,
        cascade = {CascadeType.ALL},
        mappedBy = "recipe"
    )
    @SortNatural
    private final List<RecipeInstruction> analyzedInstructions = new LinkedList<>();

    /* Many Meals to Many "Various" DietPlans
    * I.e. One "Recipe" can be in various DietPlans */
    @ManyToMany(
            fetch = FetchType.LAZY,
            cascade = {CascadeType.MERGE,CascadeType.PERSIST}
    )
    private final Set<DietPlan> dietPlans = new HashSet<>();

    public Recipe(Builder builder) {
        this.name = builder.name;
        this.type = builder.type;
        this.vegetarian = builder.vegetarian;
        this.vegan = builder.vegan;
        this.glutenFree = builder.glutenFree;
        this.dairyFree = builder.dairyFree;
        this.veryHealthy = builder.veryHealthy;
        this.cheap = builder.cheap;
        this.veryPopular = builder.veryPopular;
        this.sustainable = builder.sustainable;
        this.pricePerServing = builder.pricePerServing;
        this.instructions = builder.instructions;
        this.serving = builder.serving;
        this.readyInMinutes = builder.readyInMinutes;
    }

    public Recipe() { }

    @Accessors(fluent = true)
    @Setter
    public static class Builder{
        private String name;
        private String type;
        private Boolean vegetarian;
        private Boolean vegan;
        private Boolean glutenFree;
        private Boolean dairyFree;
        private Boolean veryHealthy;
        private Boolean cheap;
        private Boolean veryPopular;
        private Boolean sustainable;
        private Double pricePerServing;
        private String instructions;
        private Integer serving;
        private Integer readyInMinutes;

        public Recipe build(){
            return new Recipe(this);
        }
    }

    public Long instructionsSize(){
        return (long) this.analyzedInstructions.size();
    }

    public void addInstruction(RecipeInstruction instruction, int number){
        instruction.setRecipe(this);
        instruction.setNumber(number);

        this.analyzedInstructions.add(instruction);
    }

    public void removeInstruction(RecipeInstruction instruction){
        this.analyzedInstructions.remove(instruction);
        instruction.setRecipe(null);
    }

    public void addIngredient(Ingredient ingredient){
        this.ingredients.add(ingredient);
    }

    public void removeIngredient(Ingredient ingredient){
        ingredients.remove(ingredient);
        ingredient.setRecipe(null);
        ingredient.setFood(null);
    }


    /**
     * The Recipe object uses the entity identifier for equality since it lacks a unique business key.
     * We cannot hash the id since when the object is being attached and persisted (hibernate states) it will be located
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
        return this.id != null && this.id.equals(recipe.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    public Long getId() {
        return id;
    }
}

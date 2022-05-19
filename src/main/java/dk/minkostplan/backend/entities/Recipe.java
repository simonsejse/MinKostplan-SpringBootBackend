package dk.minkostplan.backend.entities;


import dk.minkostplan.backend.models.Approval;
import dk.minkostplan.backend.models.RecipeType;
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
@Setter
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

    @Column(name="name", length = 72, nullable = false)
    private String name;

    @Column(name="description", nullable = false)
    private String description;

    @Column(name="approval", nullable = false)
    @Enumerated(EnumType.STRING)
    private Approval approval;

    @Enumerated(value = EnumType.STRING)
    @Column(name="recipeType", nullable = false)
    private RecipeType type;

    @Column(name="isVegetarian", nullable = false)
    private Boolean vegetarian;

    @Column(name="isVegan", nullable = false)
    private Boolean vegan;

    @Column(name="isGlutenFree", nullable = false)
    private Boolean glutenFree;

    @Column(name="isDairyFree", nullable = false)
    private Boolean dairyFree;

    @Column(name="isVeryHealthy", nullable = false)
    private Boolean veryHealthy;

    @Column(name="isCheap", nullable = false)
    private Boolean cheap;

    @Column(name="isVeryPopular", nullable = false)
    private Boolean veryPopular;

    @Column(name="isSustainable", nullable = false)
    private Boolean sustainable;

    @Column(name="pricePerServing", nullable = false)
    private float pricePerServing;

    @Column(name="instructionsHtml", nullable = false, length = 500)
    private String instructions;

    @Column(name="readyInMinutes", nullable = false)
    private Integer readyInMinutes;

    @Column(name="image", length = 64)
    private String image;

    /* One Recipe To One User*/
    @OneToOne(
            fetch = FetchType.LAZY
    )
    private User createdBy;

    @OneToOne(
            fetch=FetchType.LAZY,
            mappedBy="recipe",
            cascade = CascadeType.ALL,
            optional = false
    )
    private Macros macros;

    /* One Recipe to Many Food Attributes */
    @OneToMany(
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
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

    @OneToMany(
            fetch = FetchType.LAZY
    )
    private List<RecipeVote> recipeVotes;

    /* Many Meals to Many "Various" DietPlans
    * I.e. One "Recipe" can be in various DietPlans */
    @ManyToMany(
            fetch = FetchType.LAZY,
            cascade = {CascadeType.MERGE,CascadeType.PERSIST}
    )
    private final Set<DietPlan> dietPlans = new HashSet<>();

    public Recipe(Builder builder) {
        this.name = builder.name;
        this.description = builder.description;
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
        this.readyInMinutes = builder.readyInMinutes;
        this.image = builder.image;
        this.approval = Approval.PENDING;
    }

    public Recipe() { }

    @Accessors(fluent = true)
    @Setter
    public static class Builder{
        private String name;
        public String description;
        private RecipeType type;
        private Boolean vegetarian;
        private Boolean vegan;
        private Boolean glutenFree;
        private Boolean dairyFree;
        private Boolean veryHealthy;
        private Boolean cheap;
        private Boolean veryPopular;
        private Boolean sustainable;
        private float pricePerServing;
        private String instructions;
        private Integer readyInMinutes;
        private String image;

        public Recipe build(){
            return new Recipe(this);
        }
    }

    public Long instructionsSize(){
        return (long) this.analyzedInstructions.size();
    }

    public void addInstruction(RecipeInstruction instruction){
        instruction.setRecipe(this);
        this.analyzedInstructions.add(instruction);
    }

    public void removeInstruction(RecipeInstruction instruction){
        this.analyzedInstructions.remove(instruction);
        instruction.setRecipe(null);
    }

    public void addIngredient(Ingredient ingredient){
        ingredient.setRecipe(this);
        this.ingredients.add(ingredient);
    }

    public void removeIngredient(Ingredient ingredient){
        ingredients.remove(ingredient);
        ingredient.setRecipe(null);
        ingredient.setFood(null);
    }


    public void setMacros(Macros macros) {
        macros.setRecipe(this);
        this.macros = macros;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
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
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Recipe other = (Recipe) obj;
        return id != null && id.equals(other.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    public Long getId() {
        return id;
    }
}

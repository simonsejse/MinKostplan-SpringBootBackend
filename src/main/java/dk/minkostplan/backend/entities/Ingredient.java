package dk.minkostplan.backend.entities;

import dk.minkostplan.backend.payload.request.recipe.IngredientRequest;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity(name = "Ingredient")
@Table(name = "ingredient")
@Getter
@Builder
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Ingredient {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator="ingredient_seq_gen"
    )
    @SequenceGenerator(
            name="ingredient_seq_gen",
            allocationSize = 1
    )
    @Column(name = "id", unique = true)
    private Long id;

    /* Many FoodAttribute to One Food */
    @ManyToOne(
            fetch = FetchType.LAZY
    )
    @JoinColumn(name="fk_food_id")
    private Food food;

    /* Many Ingredients to One Meal */
    @ManyToOne(
            fetch = FetchType.LAZY
    )
    @JoinColumn(name="fk_recipe_id")
    private Recipe recipe;

    @Column(name="amount", nullable = false)
    private float amount;

    @Column(name="instruction")
    private String instruction;

    @OneToOne(
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            optional = false
    )
    private Measurement measure;

    @ManyToMany(
            fetch = FetchType.LAZY

    )
    @JoinTable(
            joinColumns = {@JoinColumn(name="fk_ingredient")},
            inverseJoinColumns = {@JoinColumn(name="fk_meta")}
    )
    private Set<Meta> meta = new HashSet<>();

    public Ingredient(Food food, float amount, String instruction, Measurement measure, Set<Meta> meta) {
        this.food = food;
        this.amount = amount;
        this.instruction = instruction;
        this.measure = measure;
        this.meta = meta;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Ingredient)) return false;
        Ingredient that = (Ingredient) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}

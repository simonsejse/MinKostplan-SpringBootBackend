package dk.minkostplan.backend.entities;

import lombok.*;

import javax.persistence.*;

@Entity(name = "FoodAttribute")
@Table(name = "food_attribute")
@Getter
@Setter
@NoArgsConstructor
public class FoodAttribute {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator="food_attr_seq_gen"
    )
    @SequenceGenerator(
            name="food_attr_seq_gen",
            allocationSize = 1
    )
    @Column(name = "id", unique = true)
    private Long id;

    /* Many FoodAttribute to One Food */
    @ManyToOne(
            fetch = FetchType.LAZY
    )
    private Food food;

    /* Many FoodAttribute to One Meal */
    @ManyToOne(
            fetch = FetchType.LAZY
    )
    private Recipe recipe;

    public FoodAttribute(Food food, Recipe recipe, Integer amount){
        this.food = food;
        this.recipe = recipe;
        this.amount = amount;
    }
    @Column(name="amount", nullable = false)
    private Integer amount;
}

package dk.minkostplan.backend.entities;

import dk.minkostplan.backend.models.dtos.recipes.FoodDTO;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

//Static foods in db
@Getter
@Setter
@Entity(name="Food")
@Table(name="food")
public class Food {
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "food_seq_gen"
    )
    @SequenceGenerator(
            name = "food_seq_gen",
            allocationSize = 1
    )
    @Id
    @Column(name="food_id", unique = true)
    private Long id;
    @Column(name="food_type")
    private String foodType;
    /* unique business key for equality checks */
    @Column(nullable = false, name="food_name", unique = true)
    @NaturalId
    private String name;
    @Column(name="food_kj")
    private float kj;
    @Column(name="food_kcal")
    private float kcal;
    @Column(name="food_protein")
    private float protein;
    @Column(name="food_carbs")
    private float carbs;
    @Column(name="food_fat")
    private float fat;
    @Column(name="food_added_sugars")
    private float addedSugars;
    @Column(name="food_fibers")
    private float fibers;

    /* One Food To Many Ingredients */
    @OneToMany(
            fetch = FetchType.LAZY,
            cascade = {CascadeType.ALL},
            mappedBy="food"
    )
    private Set<Ingredient> ingredients = new HashSet<>();

    public Food(String foodType, String name, float kj, float kcal, float protein, float carbs, float fat, float addedSugars, float fibers) {
        this.foodType = foodType;
        this.name = name;
        this.kj = kj;
        this.kcal = kcal;
        this.protein = protein;
        this.carbs = carbs;
        this.fat = fat;
        this.addedSugars = addedSugars;
        this.fibers = fibers;
    }

    public Food(FoodDTO foodDTO){
        this.foodType = foodDTO.getFoodType();
        this.name = foodDTO.getName();
        this.kj = foodDTO.getKj();
        this.kcal = foodDTO.getKcal();
        this.protein = foodDTO.getProtein();
        this.carbs = foodDTO.getCarbs();
        this.fat = foodDTO.getFat();
        this.addedSugars = foodDTO.getAddedSugars();
        this.fibers = foodDTO.getFibers();
    }

    protected Food(){}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Food)) return false;
        Food food = (Food) o;
        return Objects.equals(getName(), food.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName());
    }
}

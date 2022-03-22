package dk.minkostplan.backend.entities;

import javax.persistence.*;
import java.util.Objects;

@Entity(name="MealInstruction")
@Table(name="meal_instructions")
public class MealInstruction implements Comparable<MealInstruction>{
    @Id
    @Column(name="instruction_id", unique = true)
    private Long id;

    @Column(name="instruction")
    private String instruction;

    //Meal owns the assocation MealInstruction
    @ManyToOne(fetch = FetchType.LAZY)
    private Recipe recipe;


    public void setMeal(Recipe recipe) {
        this.recipe = recipe;
    }

    public MealInstruction(Long id, String instruction){
        this.id = id;
        this.instruction = instruction;
    }

    protected MealInstruction() {}

    @Override
    public int compareTo(MealInstruction o) {
        return id.intValue() - o.id.intValue();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MealInstruction)) return false;
        MealInstruction that = (MealInstruction) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    public Long getId() {
        return id;
    }
    public String getInstruction() {
        return instruction;
    }


}

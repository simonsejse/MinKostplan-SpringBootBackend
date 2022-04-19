package dk.minkostplan.backend.entities;

import dk.minkostplan.backend.payload.request.recipe.InstructionCreateRequest;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;

@Entity(name="RecipeInstruction")
@Table(name="recipe_instructions")
@Getter
@Setter
public class RecipeInstruction implements Comparable<RecipeInstruction>{

    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator =  "instruction_seq_gen"
    )
    @SequenceGenerator(
            name="instruction_seq_gen",
            allocationSize = 1
    )
    @Column(name="id", unique = true)
    @Id
    private Long id;

    @Column(name = "number")
    private Integer number;

    @Column(name="instruction")
    private String step;

    //Meal owns the assocation MealInstruction
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="fk_recipe_id")
    private Recipe recipe;

    public RecipeInstruction(Integer number, String step) {
        this.number = number;
        this.step = step;
    }

    public RecipeInstruction(InstructionCreateRequest request){
        this(request.getNumber(), request.getStep());
    }

    protected RecipeInstruction() {}

    @Override
    public int compareTo(RecipeInstruction o) {
        return number - o.number;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RecipeInstruction)) return false;
        RecipeInstruction that = (RecipeInstruction) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}

package dk.minkostplan.backend.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity(name="Macros")
@Table(name="macros")
@Getter
@Setter
public class Macros {

    /**
     * Vores @Id bruger ikke længere en @GeneratedValue
     * fordi at macros id'et også kaldt (macro identifieren) bliver
     * sat af vores recipe identifier altså (id) ved brug af @MapsId.
     */
    @Id
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    private Recipe recipe;

    @Column(name="calories")
    private Integer calories;
    @Column(name="fat")
    private Integer fat;
    @Column(name="protein")
    private Integer protein;
    @Column(name="carbs")
    private Integer carbs;

    public Macros(Integer calories, Integer protein, Integer fat, Integer carbs) {
        this.calories = calories;
        this.protein = protein;
        this.fat = fat;
        this.carbs = carbs;
    }

    protected Macros(){}
}

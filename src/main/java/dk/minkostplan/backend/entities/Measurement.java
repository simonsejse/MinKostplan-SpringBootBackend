package dk.minkostplan.backend.entities;

import dk.minkostplan.backend.models.MeasureType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity(name="Measurement")
@Table(name="measurement")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Measurement {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "measurement_seq_gen")
    @SequenceGenerator(name = "measurement_seq_gen", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Long id;
    @Enumerated(EnumType.STRING)
    @Column(name="measureType", nullable = false)
    private MeasureType type;
    @Column(name="amountOfType", nullable = false)
    private float amountOfType;
    @Column(name="amountInGrams", nullable = false)
    private float amountInGrams;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public MeasureType getType() {
        return type;
    }

    public void setType(MeasureType type) {
        this.type = type;
    }

    public float getAmountOfType() {
        return amountOfType;
    }

    public void setAmountOfType(float amountOfType) {
        this.amountOfType = amountOfType;
    }

    public float getAmountInGrams() {
        return amountInGrams;
    }

    public void setAmountInGrams(float amountInGrams) {
        this.amountInGrams = amountInGrams;
    }
}

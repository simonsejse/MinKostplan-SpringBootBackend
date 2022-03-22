package dk.minkostplan.backend.entities;

import dk.minkostplan.backend.models.DietPurpose;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity(name = "DietPlan")
@Table(name = "dietplan")
public class DietPlan {
    @Id
    @GeneratedValue(
            generator = "dp_seq_gen",
            strategy = GenerationType.SEQUENCE
    )
    @SequenceGenerator(
            name="dp_seq_gen",
            allocationSize = 1
    )
    @Column(name = "dietplan_id", unique = true, nullable = false)
    private Long dietPlanId;

    @Enumerated(EnumType.STRING)
    @Column(name = "diet_purpose", nullable = false)
    private DietPurpose dietPurpose;

    @Column(name = "diet_desc", nullable = false)
    private String description;

    /* 0 if DietPurpose.MAINTENANCE
    * I.e. DietPurpose.LOSS lossOrAdditionPerWeek could potentially be 0.5 meaning a loss of 500 per week */
    @Column(name = "loss_or_addition_week", nullable = false)
    private long lossOrAdditionPerWeek;

    /* Many DietPlans can have Many Meals therefore Many:Many
    *  I.e. One "Meal" can be in various DietPlans.. */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            joinColumns = @JoinColumn(name = "fk_dietplan"),
            inverseJoinColumns = @JoinColumn(name = "fk_recipe")
    )
    private Set<Recipe> recipes = new HashSet<>();

    /* Many DietPlans to One User */
    @ManyToOne(
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL
    )
    private User user;

}

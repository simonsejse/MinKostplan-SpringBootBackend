package dk.minkostplan.backend.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table
public class Meta {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "meta_seq_gen"
    )
    @SequenceGenerator(
            name = "meta_seq_gen",
            allocationSize = 1
    )
    private Long id;

    @Column(name = "meta", unique = true)
    private String meta;

    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {CascadeType.MERGE,CascadeType.PERSIST}
    )
    public Set<Ingredient> ingredient;

    public Meta(String meta){
        this.meta = meta;
    }
}



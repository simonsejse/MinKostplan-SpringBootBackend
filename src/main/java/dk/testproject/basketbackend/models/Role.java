package dk.testproject.basketbackend.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Table(name = "role")
@Entity
@Getter
@Setter
public class Role {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "role_seq_gen"
    )
    @SequenceGenerator(
            name = "role_seq_gen",
            allocationSize = 1
    )
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "name", length = 20)
    private ERole role;

    @ManyToOne(fetch=FetchType.LAZY)
    private User user;

    public Role(ERole role, User user) {
        this.role = role;
        this.user = user;
    }
    protected Role(){

    }
}
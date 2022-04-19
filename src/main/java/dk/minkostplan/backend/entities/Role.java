package dk.minkostplan.backend.entities;

import dk.minkostplan.backend.models.ERole;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity(name="Role")
@Table(name="roles")
public class Role {

    @Column(name="role_id")
    @GeneratedValue(
           strategy = GenerationType.SEQUENCE,
           generator = "role_seq_gen"
    )
    @SequenceGenerator(
            name = "role_seq_gen",
            allocationSize = 1
    )
    @Id
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Enumerated(value=EnumType.STRING)
    @Column(name="role_name", unique = true)
    private ERole name;
    public String getName() {
        return name.name();
    }
    public void setName(ERole eRole) {
        this.name = eRole;
    }

    @ManyToMany(
            fetch = FetchType.LAZY,
            mappedBy = "roles"
    )
    private Set<User> users = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Role)) return false;
        Role role = (Role) o;
        return this.id != null && Objects.equals(getId(), role.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    public Role(ERole name) {
        this.name = name;
    }
    protected Role() { }
}

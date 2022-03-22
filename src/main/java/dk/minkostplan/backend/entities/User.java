package dk.minkostplan.backend.entities;

import dk.minkostplan.backend.models.Role;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Setter
@Getter
@Table(name="user")
@Entity
public class User {

    @Id
    @Column(name = "user_id", unique = true)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq_gen")
    @SequenceGenerator(name = "user_seq_gen", allocationSize = 1)
    private Long id;

    //Email
    @Column(name = "name", unique = true)
    private String username;

    //Email
    @Column(name = "user_email", unique = true)
    private String email;

    @Column(name = "user_pw")
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_role", length = 20)
    private Role role;

    /* One User Can Have Many DietPlans */
    @OneToMany(
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            mappedBy="user"
    )
    private List<DietPlan> dietPlans;


    @Column(name = "account_enabled")
    private boolean accountEnabled;
    @Column(name = "credentials_expired")
    private boolean credentialsExpired;
    @Column(name = "account_locked")
    private boolean accountLocked;
    @Column(name = "account_expired")
    private boolean accountExpired;

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = Role.ROLE_USER;
        this.accountEnabled = true;
        this.credentialsExpired = false;
        this.accountLocked = false;
        this.accountExpired = false;
    }

    protected User(){}
}

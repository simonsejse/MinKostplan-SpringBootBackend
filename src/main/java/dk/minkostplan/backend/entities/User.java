package dk.minkostplan.backend.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    @Column(name="recipes_made")
    private int totalRecipesMade;

    @ManyToMany(
            fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            }
    )
    @JoinTable(
            joinColumns = {@JoinColumn(name="user_id")},
            inverseJoinColumns = {@JoinColumn(name="role_id")}
    )
    private Set<Role> roles = new HashSet<>();

    /* One User Can Have Many DietPlans */
    @OneToMany(
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            mappedBy="user"
    )
    private List<DietPlan> dietPlans = new ArrayList<>();

    @OneToMany(
            fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            }
    )
    private List<Ticket> tickets = new ArrayList<>();

    @Column(name = "account_enabled")
    private boolean accountEnabled;
    @Column(name = "credentials_expired")
    private boolean credentialsExpired;
    @Column(name = "account_locked")
    private boolean accountLocked;
    @Column(name = "account_expired")
    private boolean accountExpired;


    public void createNewTicket(Ticket ticket){
        ticket.setSubmittedBy(this);
        this.tickets.add(ticket);
    }

    public User(String username, String email, String password, Role role) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.roles.add(role);
        this.accountEnabled = true;
        this.credentialsExpired = false;
        this.accountLocked = false;
        this.accountExpired = false;
    }

    protected User(){}

    public void incrementRecipesTotalMade() {
        this.totalRecipesMade++;
    }
}

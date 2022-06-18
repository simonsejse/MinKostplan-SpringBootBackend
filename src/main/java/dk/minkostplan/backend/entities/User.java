package dk.minkostplan.backend.entities;

import dk.minkostplan.backend.models.ActivityState;
import dk.minkostplan.backend.models.Gender;
import jdk.jfr.Timestamp;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Table(name="user")
@Entity
public class User {

    @Id
    @Column(name = "user_id", unique = true)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq_gen")
    @SequenceGenerator(name = "user_seq_gen", allocationSize = 1)
    @Getter
    private Long id;

    //Email
    @Column(name = "name", unique = true, nullable = false)
    @Getter @Setter
    private String username;

    //Email
    @Column(name = "user_email", unique = true, nullable = false)
    @Getter @Setter
    private String email;

    @Column(name = "user_pw", nullable = false)
    @Getter @Setter
    private String password;

    @Column(name="recipes_made", nullable = false)
    @Getter @Setter
    private int totalRecipesMade;

    @Enumerated(EnumType.STRING)
    @Column(name="activity_state", nullable = false)
    @Getter @Setter
    private ActivityState activityState;

    @Enumerated(EnumType.STRING)
    @Column(name="gender_state", nullable = false)
    @Getter @Setter
    private Gender genderState;

    @Getter
    @Setter
    @Column(name="weight", nullable = false)
    private int weight;

    @Getter
    @Setter
    @Column(name="height", nullable = false)
    private int height;

    @Timestamp
    @Column(name = "born_when", nullable = false)
    private LocalDate birthday;

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
    @Getter @Setter
    private Set<Role> roles = new HashSet<>();

    /* One User Can Have Many DietPlans */
    @OneToMany(
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            mappedBy="user"
    )
    @Getter @Setter
    private List<DietPlan> dietPlans = new ArrayList<>();

    @OneToMany(
            fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            }
    )
    private List<Ticket> tickets = new ArrayList<>();

    @Getter @Setter
    @Column(name = "account_enabled")
    private boolean accountEnabled;

    @Column(name = "credentials_expired")
    @Getter @Setter
    private boolean credentialsExpired;

    @Column(name = "account_locked")
    @Getter @Setter
    private boolean accountLocked;

    @Column(name = "account_expired")
    @Getter @Setter
    private boolean accountExpired;


    public void createNewTicket(Ticket ticket){
        ticket.setSubmittedBy(this);
        this.tickets.add(ticket);
    }

    public User(String username, String email, String password, Role role, ActivityState activityState, Gender genderState, int weight, int height, LocalDate birthday) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.birthday = birthday;
        this.roles.add(role);
        this.activityState = activityState;
        this.genderState = genderState;
        this.weight = weight;
        this.height = height;
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

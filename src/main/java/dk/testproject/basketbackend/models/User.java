package dk.testproject.basketbackend.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

@Setter
@Getter
@Table(name="user")
@Entity
public class User {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq_gen")
    @SequenceGenerator(name = "user_seq_gen", allocationSize = 1)
    private Long id;

    //Email
    @Column(name = "name", unique = true)
    private String username;

    //Email
    @Column(name = "email", unique = true)
    private String email;

    @JsonIgnore
    @Column(name = "password")
    private String password;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private List<Role> roles;

    @JsonIgnore
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
        this.roles = List.of(new Role(ERole.ROLE_USER));
        this.accountEnabled = true;
        this.credentialsExpired = false;
        this.accountLocked = false;
        this.accountExpired = false;
    }

    protected User(){}
}

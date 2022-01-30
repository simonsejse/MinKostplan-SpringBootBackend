package dk.testproject.basketbackend.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
@Setter
@Getter
@Entity
public class User {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq_gen")
    @SequenceGenerator(name = "user_seq_gen", allocationSize = 1)
    private Long id;

    //Email
    @Column(name = "email")
    private String email;

    @JsonIgnore
    @Column(name = "password")
    private String password;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "user")
    private Set<Role> roles = new HashSet<>();

    @JsonIgnore
    @Column(name = "account_enabled")
    private boolean accountEnabled;
    @Column(name = "credentials_expired")
    private boolean credentialsExpired;
    @Column(name = "account_locked")
    private boolean accountLocked;
    @Column(name = "account_expired")
    private boolean accountExpired;

    @Column(name = "session_id")
    private String sid;

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public Set<Role> getRoles() {
        return roles;
    }


    public User(Long id, String email, String password, Set<Role> roles, boolean accountEnabled, boolean credentialsExpired, boolean accountLocked, boolean accountExpired) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.roles = roles;
        this.accountEnabled = accountEnabled;
        this.credentialsExpired = credentialsExpired;
        this.accountLocked = accountLocked;
        this.accountExpired = accountExpired;
    }

    protected User(){}

    public UserDetails build() {
        return null;
    }
}

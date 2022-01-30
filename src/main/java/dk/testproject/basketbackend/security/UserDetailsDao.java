package dk.testproject.basketbackend.security;

import dk.testproject.basketbackend.models.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Collectors;

public class UserDetailsDao implements UserDetails {

    private final String username;
    private final String password;
    private final boolean isAccountExpired;
    private final boolean isAccountLocked;
    private final boolean isCredentialsExpired;
    private final boolean isEnabled;
    private final Collection<? extends GrantedAuthority> authorities;

    public UserDetailsDao(User user) {
        this.username = user.getEmail();
        this.password = user.getPassword();
        this.isAccountExpired = user.isAccountExpired();
        this.isAccountLocked = user.isAccountLocked();
        this.isCredentialsExpired = user.isCredentialsExpired();
        this.isEnabled = user.isAccountEnabled();
        this.authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getRole().name()))
                .collect(Collectors.toList());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}

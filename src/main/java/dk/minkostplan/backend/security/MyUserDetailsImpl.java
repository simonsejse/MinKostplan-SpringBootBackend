package dk.minkostplan.backend.security;

import dk.minkostplan.backend.entities.Role;
import dk.minkostplan.backend.entities.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

public class MyUserDetailsImpl implements org.springframework.security.core.userdetails.UserDetails {

    private final String username;
    private final String password;
    private final boolean isAccountExpired;
    private final boolean isAccountLocked;
    private final boolean isCredentialsExpired;
    private final boolean isEnabled;
    private final Collection<? extends GrantedAuthority> authorities;

    public MyUserDetailsImpl(User user) {
        this.username = user.getEmail();
        this.password = user.getPassword();
        this.isAccountExpired = user.isAccountExpired();
        this.isAccountLocked = user.isAccountLocked();
        this.isCredentialsExpired = user.isCredentialsExpired();
        this.isEnabled = user.isAccountEnabled();
        this.authorities = user.getRoles()
                .stream()
                .map(Role::getName)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return !this.isAccountExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !this.isAccountLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return !this.isCredentialsExpired;
    }

    @Override
    public boolean isEnabled() {
        return this.isEnabled;
    }

}

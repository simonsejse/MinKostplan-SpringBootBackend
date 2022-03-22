package dk.minkostplan.backend.security;

import dk.minkostplan.backend.service.UserService;
import dk.minkostplan.backend.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {
    private final UserService userService;

    @Autowired
    public MyUserDetailsService(UserService userService) {
        this.userService = userService;
    }

    /**
     * This is called after JSONUsernamePasswordAuthenticationFilter
     * that checks if the authentication can be authenticated
     *
     * @IMPORTANT:
     * This is how the program uses antMatchers to determine if the user has Role.ADMIN for say because we return a
     * MyUserDetailsImpl that contains a Collection of authories:
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User userByUsername = userService.getUserByUsername(username);
        return new MyUserDetailsImpl(userByUsername);
    }

}

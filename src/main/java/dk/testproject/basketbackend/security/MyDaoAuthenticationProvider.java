package dk.testproject.basketbackend.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class MyDaoAuthenticationProvider extends DaoAuthenticationProvider {
    @Autowired
    public MyDaoAuthenticationProvider(UserDetailsService myUserDetailsService, PasswordEncoder passwordEncoder) {
        this.setUserDetailsService(myUserDetailsService);
        this.setPasswordEncoder(passwordEncoder);
   }
}

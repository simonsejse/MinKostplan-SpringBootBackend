package dk.testproject.basketbackend.security.rememberme;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;

/**
 * Creates an instance inside our security config since it needs parameters like a key
 */
public class MyCustomTokenBasedRememberMeService extends TokenBasedRememberMeServices {
    public MyCustomTokenBasedRememberMeService(String key, UserDetailsService userDetailsService) {
        super(key, userDetailsService);
    }
}

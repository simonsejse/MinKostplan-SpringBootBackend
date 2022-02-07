package dk.testproject.basketbackend.security;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.RememberMeAuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@EnableWebSecurity
@Order(2)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private final Logger log = Logger.getLogger(WebSecurityConfig.class.getName());

    private final CorsConfigurationSource corsConfigurationSource;
    private final RememberMeServices myCustomTokenBasedRememberMeService;
    private final RememberMeAuthenticationProvider rememberMeAuthenticationProvider;

    private final DaoAuthenticationProvider authProvider;
    @Autowired
    public WebSecurityConfig(CorsConfigurationSource corsConfigurationSource, RememberMeServices myCustomTokenBasedRememberMeService, RememberMeAuthenticationProvider rememberMeAuthenticationProvider, DaoAuthenticationProvider authProvider) {
        this.corsConfigurationSource = corsConfigurationSource;
        this.myCustomTokenBasedRememberMeService = myCustomTokenBasedRememberMeService;
        this.rememberMeAuthenticationProvider = rememberMeAuthenticationProvider;
        this.authProvider = authProvider;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .authenticationProvider(authProvider)
                .authenticationProvider(rememberMeAuthenticationProvider);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors()
                .configurationSource(corsConfigurationSource)
                .and()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                .and()
                .formLogin().disable()
                .rememberMe().rememberMeServices(myCustomTokenBasedRememberMeService)
                .and()
                .logout().logoutSuccessHandler((request, response, authentication) -> {
                     response.setStatus(200);
                    response.getWriter().write("OK");

                    log.info(authentication + " has logged out!");
                })
                .and()
                .authorizeRequests()
                    .antMatchers("/resources/**").permitAll()
                    .antMatchers(HttpMethod.POST, "/login").permitAll()
                    .antMatchers(HttpMethod.POST, "/signup").permitAll()
                .anyRequest().authenticated();
    }
}

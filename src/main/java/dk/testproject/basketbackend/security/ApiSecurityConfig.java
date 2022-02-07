package dk.testproject.basketbackend.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import dk.testproject.basketbackend.security.authenticationhandlers.HandleAuthenticationFailure;
import dk.testproject.basketbackend.security.authenticationhandlers.HandleAuthenticationSuccess;
import dk.testproject.basketbackend.security.rememberme.MyCustomTokenBasedRememberMeService;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.RememberMeAuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.RememberMeAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;


@Configuration
@EnableWebSecurity
@Order(1)
public class ApiSecurityConfig extends WebSecurityConfigurerAdapter {
    private final Logger log = Logger.getLogger(ApiSecurityConfig.class.getName());

    private final DaoAuthenticationProvider authProvider;
    private final HandleAuthenticationSuccess handleAuthenticationSuccess;
    private final HandleAuthenticationFailure handleAuthenticationFailure;
    private final MyUserDetailsService myUserDetailsService;
    private final CorsConfigurationSource corsConfigurationSource;

    private static final String key = "dsaopkdaspoksadopkdasopk";

    @Autowired
    public ApiSecurityConfig(DaoAuthenticationProvider myDaoAuthenticationProvider, HandleAuthenticationSuccess handleAuthenticationSuccess, HandleAuthenticationFailure handleAuthenticationFailure, MyUserDetailsService myUserDetailsService, CorsConfigurationSource corsConfigurationSource) {
        this.authProvider = myDaoAuthenticationProvider;
        this.handleAuthenticationSuccess = handleAuthenticationSuccess;
        this.handleAuthenticationFailure = handleAuthenticationFailure;
        this.myUserDetailsService = myUserDetailsService;
        this.corsConfigurationSource = corsConfigurationSource;
    }

    /**
     * Including our RememberMeAuthenticationProvider implementation in our AuthenticationManager.setProviders() list:
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth
                .authenticationProvider(authProvider)
                .authenticationProvider(rememberMeAuthenticationProvider());

    }

    @Bean
    public JSONUsernamePasswordAuthFilter jsonUsernamePasswordAuthFilter() throws Exception {
        JSONUsernamePasswordAuthFilter jsonUsernamePasswordAuthFilter = new JSONUsernamePasswordAuthFilter();
        jsonUsernamePasswordAuthFilter.setFilterProcessesUrl("/login");

        //Sætter vores egen custom remember-me service!
        jsonUsernamePasswordAuthFilter.setRememberMeServices(myCustomTokenBasedRememberMeService());
        //Sætter vores authentication manager
        jsonUsernamePasswordAuthFilter.setAuthenticationManager(authenticationManager());

        jsonUsernamePasswordAuthFilter.setAuthenticationSuccessHandler(this.handleAuthenticationSuccess);
        jsonUsernamePasswordAuthFilter.setAuthenticationFailureHandler(this.handleAuthenticationFailure);

        return jsonUsernamePasswordAuthFilter;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .antMatcher("/api/**")
                .cors().configurationSource(corsConfigurationSource)
                .and()
                .csrf().disable()
                .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                .and()
                /**
                 * Don't need this since it's only for api so i don't need a filter for username
                    .addFilterBefore(jsonUsernamePasswordAuthFilter(), UsernamePasswordAuthenticationFilter.class)
                 */
                .authorizeRequests()
                    .antMatchers(HttpMethod.GET, "/v1/api/user").hasAnyRole("USER", "ADMIN")
                .anyRequest().authenticated()
                .and()
                .addFilterBefore(rememberMeAuthenticationFilter(), BasicAuthenticationFilter.class)
                .exceptionHandling()
                    .authenticationEntryPoint((request, response, authException) -> {
                        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

                        final Map<String, Object> body = new HashMap<>();
                        body.put("status", HttpServletResponse.SC_UNAUTHORIZED);
                        body.put("error", "Unauthorized");
                        body.put("message", authException.getMessage());
                        body.put("path", request.getServletPath());

                        final ObjectMapper mapper = new ObjectMapper();
                        mapper.writeValue(response.getOutputStream(), body);
                    });
    }

    /**
     *
     * @return AuthenticationManager Bean
     */
    @Bean @Override protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    /**
     * //RememberMeService
     * @return our MyCustomTokenBasedRememberMeService
     */
    @Bean public MyCustomTokenBasedRememberMeService myCustomTokenBasedRememberMeService() {
        MyCustomTokenBasedRememberMeService rememberMeService = new MyCustomTokenBasedRememberMeService(key, myUserDetailsService);
        rememberMeService.setCookieName("remember-me");
        rememberMeService.setUseSecureCookie(true);
        return rememberMeService;
    }

    /**
     *
     * @return a RememberMeAuthenticationFilter filter for filtering the remember-me token
     * checking if the remember-me cookies is present and if it is,
     * it uses the authenticationManager to login using authenticationManager#authenticate
     */
    @Bean public RememberMeAuthenticationFilter rememberMeAuthenticationFilter() throws Exception {
        return new RememberMeAuthenticationFilter(authenticationManager(), myCustomTokenBasedRememberMeService());
    }

    @Bean public RememberMeAuthenticationProvider rememberMeAuthenticationProvider(){
        return new RememberMeAuthenticationProvider(key);
    }

}

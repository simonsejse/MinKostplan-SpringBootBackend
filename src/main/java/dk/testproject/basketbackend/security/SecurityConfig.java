package dk.testproject.basketbackend.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import dk.testproject.basketbackend.security.authenticationhandlers.HandleAuthenticationFailure;
import dk.testproject.basketbackend.security.authenticationhandlers.HandleAuthenticationSuccess;
import dk.testproject.basketbackend.security.rememberme.MyCustomTokenBasedRememberMeService;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final Logger log = Logger.getLogger(SecurityConfig.class.getName());

    private final DaoAuthenticationProvider authProvider;
    private final HandleAuthenticationSuccess handleAuthenticationSuccess;
    private final HandleAuthenticationFailure handleAuthenticationFailure;
    private final MyUserDetailsService myUserDetailsService;
    private static final String key = "dsaopkdaspoksadopkdasopk";

    @Autowired
    public SecurityConfig(DaoAuthenticationProvider myDaoAuthenticationProvider, HandleAuthenticationSuccess handleAuthenticationSuccess, HandleAuthenticationFailure handleAuthenticationFailure, MyUserDetailsService myUserDetailsService) {
        this.authProvider = myDaoAuthenticationProvider;
        this.handleAuthenticationSuccess = handleAuthenticationSuccess;
        this.handleAuthenticationFailure = handleAuthenticationFailure;
        this.myUserDetailsService = myUserDetailsService;
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
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000"));
        configuration.setAllowedMethods(Arrays.asList("GET","POST"));
        configuration.setAllowCredentials(true);
        configuration.setAllowedHeaders(Arrays.asList("Access-Control-Allow-Origin","Origin", "Content-Type", "Accept","Authorization"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**",configuration);
        return source;
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
                .cors().configurationSource(corsConfigurationSource())
                .and()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                .and()
                .rememberMe().rememberMeServices(myCustomTokenBasedRememberMeService())
                .and()
                .addFilterBefore(jsonUsernamePasswordAuthFilter(), UsernamePasswordAuthenticationFilter.class)
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
                }).and()
                .formLogin().disable()
                .logout().logoutSuccessHandler((request, response, authentication) -> {
                    response.setStatus(200);
                    response.getWriter().write("OK");

                    log.info(authentication + " has logged out!");
                })
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, "/login").permitAll()
                .antMatchers(HttpMethod.POST, "/signup").permitAll()
             //   .antMatchers(HttpMethod.GET, "/v1/api/user").hasAnyRole("USER", "ADMIN")
                .anyRequest().authenticated();
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

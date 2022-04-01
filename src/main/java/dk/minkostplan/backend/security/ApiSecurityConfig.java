package dk.minkostplan.backend.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import dk.minkostplan.backend.security.authenticationhandlers.HandleAuthenticationFailure;
import dk.minkostplan.backend.security.authenticationhandlers.HandleAuthenticationSuccess;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.access.expression.SecurityExpressionHandler;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.RememberMeAuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.authentication.rememberme.RememberMeAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
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
    private final ObjectMapper mapper;
    private static final String key = "dsaopkdaspoksadopkdasopk";

    @Autowired
    public ApiSecurityConfig(DaoAuthenticationProvider myDaoAuthenticationProvider, HandleAuthenticationSuccess handleAuthenticationSuccess, HandleAuthenticationFailure handleAuthenticationFailure, MyUserDetailsService myUserDetailsService, CorsConfigurationSource corsConfigurationSource, @Qualifier("objectMapper") ObjectMapper mapper) {
        this.authProvider = myDaoAuthenticationProvider;
        this.handleAuthenticationSuccess = handleAuthenticationSuccess;
        this.handleAuthenticationFailure = handleAuthenticationFailure;
        this.myUserDetailsService = myUserDetailsService;
        this.corsConfigurationSource = corsConfigurationSource;
        this.mapper = mapper;
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
        JSONUsernamePasswordAuthFilter jsonUsernamePasswordAuthFilter = new JSONUsernamePasswordAuthFilter(mapper);
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
                /*Don't need this since it's only for api so i don't need a filter for username
                 .addFilterBefore(jsonUsernamePasswordAuthFilter(), UsernamePasswordAuthenticationFilter.class)*/
                .authorizeRequests()
                    .expressionHandler(webExpressionHandler())
                    .antMatchers(HttpMethod.POST, "/api/recipes/**").permitAll()
                    .antMatchers(HttpMethod.GET, "/api/recipes/**").permitAll()
                    .antMatchers(HttpMethod.DELETE, "/api/recipes/delete/**").permitAll()
                    .antMatchers(HttpMethod.GET, "/v1/api/user").hasAnyRole("USER", "ADMIN")
                    .antMatchers(HttpMethod.GET, "/api/foods").hasRole("ADMIN")
                    .antMatchers(HttpMethod.POST, "/api/diet-plans/create-diet-plan").hasRole("USER")
                .anyRequest().authenticated()
                .and()
                .addFilterBefore(rememberMeAuthenticationFilter(), BasicAuthenticationFilter.class)
                .exceptionHandling()
                    .authenticationEntryPoint((request, response, authException) -> {
                        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

                        final Map<String, Object> body = new HashMap<>();
                        body.put("status", HttpServletResponse.SC_UNAUTHORIZED);
                        body.put("timestamp", LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));
                        body.put("error", "Unauthorized");
                        //authException.getMessage()
                        body.put("message", "Du har ikke tilladdelse til den her del af APIen!");
                        body.put("path", request.getServletPath());
                        mapper.writeValue(response.getOutputStream(), body);
                    });
    }
    /**
     * Role hierarchy
     */
    @Bean
    public SecurityExpressionHandler<FilterInvocation> webExpressionHandler(){
        DefaultWebSecurityExpressionHandler expressionHandler = new DefaultWebSecurityExpressionHandler();
        expressionHandler.setRoleHierarchy(roleHierarchy());
        return expressionHandler;
    }

    @Bean
    public RoleHierarchy roleHierarchy(){
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
        String hierarchy = "ROLE_ADMIN > ROLE_MODERATOR \n " +
                "ROLE_MODERATOR > ROLE_VIP_UNLIMITED \n " +
                "ROLE_VIP_UNLIMITED > ROLE_VIP \n " +
                "ROLE_VIP > ROLE_USER";
        roleHierarchy.setHierarchy(hierarchy);
        return roleHierarchy;
    }

    /**
     * public enum ERole {
     *     ROLE_USER,
     *     ROLE_VIP,
     *     ROLE_VIP_UNLIMITED,
     *     ROLE_MODERATOR,
     *     ROLE_ADMIN
     * }
     */


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
    @Bean public TokenBasedRememberMeServices myCustomTokenBasedRememberMeService() {
        /*rememberMeService.setCookieName("remember-me");*/
        final TokenBasedRememberMeServices rememberMeService = new TokenBasedRememberMeServices(key, myUserDetailsService);
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

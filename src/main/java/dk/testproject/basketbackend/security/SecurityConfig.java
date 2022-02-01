package dk.testproject.basketbackend.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import dk.testproject.basketbackend.security.authenticationhandlers.HandleAuthenticationFailure;
import dk.testproject.basketbackend.security.authenticationhandlers.HandleAuthenticationSuccess;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

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

    @Autowired
    public SecurityConfig(DaoAuthenticationProvider myDaoAuthenticationProvider, HandleAuthenticationSuccess handleAuthenticationSuccess, HandleAuthenticationFailure handleAuthenticationFailure) {
        this.authProvider = myDaoAuthenticationProvider;
        this.handleAuthenticationSuccess = handleAuthenticationSuccess;
        this.handleAuthenticationFailure = handleAuthenticationFailure;
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

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(authProvider);
    }

    @Bean
    public JSONUsernamePasswordAuthFilter jsonUsernamePasswordAuthFilter() throws Exception {
        JSONUsernamePasswordAuthFilter jsonUsernamePasswordAuthFilter = new JSONUsernamePasswordAuthFilter();
        jsonUsernamePasswordAuthFilter.setFilterProcessesUrl("/login");

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
                .addFilterBefore(jsonUsernamePasswordAuthFilter(), UsernamePasswordAuthenticationFilter.class)
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

        /**
         *  .authorizeRequests()
         *                 .antMatchers(HttpMethod.GET,"/welcome").permitAll()
         *                 .antMatchers(HttpMethod.POST,"/signup").permitAll()
         *                 .antMatchers("/actuator/**").hasAnyRole("ADMIN")
         *                 .antMatchers("/admin/**").hasAnyRole("ADMIN")
         *                 .antMatchers("/stats").hasAnyAuthority("VIEW_STATS")
         *                 .anyRequest().authenticated();
         */
       /* http

                .and()
              //  .addFilterBefore(new UsernamePasswordAuthFilter(), BasicAuthenticationFilter.class)
               // .addFilterBefore(new CookieAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
                .and()
                .formLogin()
                .successHandler((request, response, authentication) -> {
                    response.setStatus(200);
                    response.getWriter().write("OK");
                })
                .loginPage("/login").permitAll()
                .successForwardUrl("/mig")
                .failureForwardUrl("/login")
                .and()
                .logout()
                .deleteCookies(DeleteMeOldCookieFilter.COOKIE_NAME)
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, "/api/auth/signIn", "/api/auth/signUn").permitAll()
                .anyRequest().authenticated();

        */
    }

}

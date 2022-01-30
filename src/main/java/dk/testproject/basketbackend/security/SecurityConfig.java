package dk.testproject.basketbackend.security;

import dk.testproject.basketbackend.bin.DeleteMeOldCookieFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UnauthorizedHandlingEntryPoint unauthorizedHandlingEntryPoint;

    @Autowired
    public SecurityConfig(UnauthorizedHandlingEntryPoint unauthorizedHandlingEntryPoint) {
        this.unauthorizedHandlingEntryPoint = unauthorizedHandlingEntryPoint;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors().disable()
                .csrf().disable()
                .formLogin()
                .permitAll()
                .and()
                .authorizeRequests()
                .anyRequest().authenticated();
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

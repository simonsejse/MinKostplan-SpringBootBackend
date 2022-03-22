package dk.minkostplan.backend.bin;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Cookie Authentication Filter for checking if user has cookie and filter it!
 *  it uses SecurityContextHolder to check if it does not have Authentication then it will go to
 *  UnauthorizedHandlingEntryPoint
 */
public class DeleteMeOldCookieFilter extends OncePerRequestFilter {
    public static final String COOKIE_NAME = "AUTH_BY_COOKIE";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Optional<Cookie> cookieAuth = Stream
                .of(Optional.ofNullable(request.getCookies()).orElse(new Cookie[0]))
                .filter(cookie -> COOKIE_NAME.equals(cookie.getName()))
                .findFirst();

        cookieAuth.ifPresent(cookie -> {
            SecurityContextHolder.getContext().setAuthentication(
                    new PreAuthenticatedAuthenticationToken(cookie.getValue(), null)
            );
        });
        filterChain.doFilter(request, response);
    }
}

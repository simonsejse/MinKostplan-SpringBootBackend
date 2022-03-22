package dk.minkostplan.backend.security;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class JSONUsernamePasswordAuthFilter extends UsernamePasswordAuthenticationFilter {

    private final Logger log = Logger.getLogger(JSONUsernamePasswordAuthFilter.class.getName());
    private final ObjectMapper mapper;

    @Autowired
    public JSONUsernamePasswordAuthFilter(@Qualifier("objectMapper") ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @SneakyThrows
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        if(Objects.equals(request.getMethod(), RequestMethod.OPTIONS.name())){
            return null;
        }
        String login = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));

        JsonNode loginJson = mapper.readTree(login);

        String username = loginJson.get("username").asText();
        final String password = loginJson.get("password").asText();

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password, null);
        log.info("Authentication attempt by : " + username);

        return getAuthenticationManager().authenticate(authToken);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        super.doFilter(request, response, chain);
    }
}

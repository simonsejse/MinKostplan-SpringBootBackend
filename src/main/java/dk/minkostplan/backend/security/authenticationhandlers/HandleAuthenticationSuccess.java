package dk.minkostplan.backend.security.authenticationhandlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import dk.minkostplan.backend.service.UserService;
import dk.minkostplan.backend.payload.response.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Logger;

@Component
public class HandleAuthenticationSuccess implements AuthenticationSuccessHandler {

    private final UserService userService;
    private final Logger log = Logger.getLogger(HandleAuthenticationSuccess.class.getName());
    private final ObjectMapper mapper;

    @Autowired
    public HandleAuthenticationSuccess(UserService userService, @Qualifier("objectMapper") ObjectMapper mapper) {
        this.userService = userService;
        this.mapper = mapper;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info(authentication.getName() + " authenticated successfully!");
        final UserDTO userDTOByUsername = userService.getUserDTOByUsername(authentication.getName());
        response.setStatus(200);
        mapper.writeValue(response.getOutputStream(), userDTOByUsername);
    }
}

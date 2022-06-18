package dk.minkostplan.backend.controllers;

import dk.minkostplan.backend.config.SmtpMailSender;
import dk.minkostplan.backend.payload.request.RegisterRequest;
import dk.minkostplan.backend.payload.response.UserDTO;
import dk.minkostplan.backend.service.AuthenticationService;
import dk.minkostplan.backend.service.UserService;
import jdk.swing.interop.SwingInterOpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Validated
@RestController
@RequestMapping("/")
public class AuthController {

    //TODO: Change to Authentication service
    private final UserService userService;
    private final AuthenticationService authenticationService;
    private final SmtpMailSender mailSender;

    @Autowired
    public AuthController(final UserService userService, AuthenticationService authenticationService, SmtpMailSender mailSender){
        this.userService = userService;
        this.authenticationService = authenticationService;
        this.mailSender = mailSender;
    }

    @PostMapping("signup")
    public ResponseEntity<UserDTO> signup(@RequestBody @Valid RegisterRequest registerRequest) {
        UserDTO newUser = userService.createNewUser(registerRequest);
        return ResponseEntity.ok(newUser);
    }

    @GetMapping("isAuthenticated")
    public ResponseEntity<Boolean> isAuthenticated(Authentication authentication){
        return authentication == null ? ResponseEntity.ok(false) : ResponseEntity.ok(true);
    }

    @GetMapping("reset/credentials")
    public ResponseEntity<String> resetCredentials(@RequestParam("email") String email, HttpServletRequest req) throws MessagingException {
        return new ResponseEntity<>(
                authenticationService.createResetCredentialsRequest(email, req),
                HttpStatus.OK
        );
    }



}

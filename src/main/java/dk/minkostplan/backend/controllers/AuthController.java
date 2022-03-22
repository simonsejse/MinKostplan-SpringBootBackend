package dk.minkostplan.backend.controllers;

import dk.minkostplan.backend.payload.response.UserDTO;
import dk.minkostplan.backend.service.UserService;
import dk.minkostplan.backend.payload.request.CredentialsDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
public class AuthController {

    private final UserService userService;

    @Autowired
    public AuthController(final UserService userService){
        this.userService = userService;
    }

    @PostMapping("signup")
    public ResponseEntity<UserDTO> signup(@RequestBody CredentialsDto credentialsDto) {
        return this.userService.createNewUser(credentialsDto);
    }

    @GetMapping("isAuthenticated")
    public ResponseEntity<Boolean> isAuthenticated(Authentication authentication){
        return ResponseEntity.ok(authentication.isAuthenticated());
    }

}

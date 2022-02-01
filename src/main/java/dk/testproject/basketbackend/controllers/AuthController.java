package dk.testproject.basketbackend.controllers;

import dk.testproject.basketbackend.models.User;
import dk.testproject.basketbackend.payload.request.CredentialsDto;
import dk.testproject.basketbackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class AuthController {

    private final UserService userService;

    @Autowired
    public AuthController(final UserService userService){
        this.userService = userService;
    }

    @PostMapping("signup")
    public ResponseEntity<User> signup(@RequestBody CredentialsDto credentialsDto){
        return this.userService.createNewUser(credentialsDto);
    }
}

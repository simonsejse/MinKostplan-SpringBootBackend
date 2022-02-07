package dk.testproject.basketbackend.controllers;

import dk.testproject.basketbackend.models.User;
import dk.testproject.basketbackend.payload.response.UserDTO;
import dk.testproject.basketbackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("v1/api/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @CrossOrigin("http://localhost:3000/")
    @GetMapping
    public ResponseEntity<UserDTO> index(Authentication authentication) {
        final UserDTO userByUsername = this.userService.getUserDTOByUsername(authentication.getName());
        return ResponseEntity.ok(userByUsername);
    }
}
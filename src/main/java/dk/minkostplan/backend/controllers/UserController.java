package dk.minkostplan.backend.controllers;

import dk.minkostplan.backend.payload.response.SidebarUserDTO;
import dk.minkostplan.backend.service.UserService;
import dk.minkostplan.backend.payload.response.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    //authenticationEntryPoint automatically rejects if not access and sends 401 back!
    @CrossOrigin("http://localhost:3000/")
    @GetMapping("/")
    public ResponseEntity<UserDTO> getUserByEmail(Authentication authentication) {
        UserDTO userDTOByUsername = userService.getUserDTOByUsername(authentication.getName());
        return ResponseEntity.ok(userDTOByUsername);
    }

    @CrossOrigin("http://localhost:3000/")
    @GetMapping("/info-for-sidebar")
    public ResponseEntity<SidebarUserDTO> getSidebarUserDTO(Authentication authentication){
        if (authentication == null){
            throw new RuntimeException("You're not logged in!");
        }
        SidebarUserDTO userDTO = userService.getSidebarUserDTOByUserEmail(authentication.getName());
        System.out.println(userDTO.toString());
        return ResponseEntity.ok(userDTO);
    }



}
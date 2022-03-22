package dk.minkostplan.backend.service;

import dk.minkostplan.backend.entities.User;
import dk.minkostplan.backend.payload.request.CredentialsDto;
import dk.minkostplan.backend.payload.response.UserDTO;
import dk.minkostplan.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User getUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.getUserByEmail(email)
                .orElseThrow(
                        () -> new UsernameNotFoundException("Brugeren findes ikke i databasen!")
                );
    }

    public UserDTO getUserDTOByUsername(String email) throws UsernameNotFoundException {
        return new UserDTO(getUserByUsername(email));
    }

    public ResponseEntity<UserDTO> createNewUser(CredentialsDto credentialsDto) {
        final User user = this.userRepository.save(new User(
                credentialsDto.getUsername(),
                credentialsDto.getEmail(),
                passwordEncoder.encode(credentialsDto.getPassword())
        ));
        return ResponseEntity.ok().body(
                new UserDTO(user)
        );
    }


    public void hello(){

        //Integer.parseInt()
        Function<String,Integer> bum = Integer::parseInt;
    }


}

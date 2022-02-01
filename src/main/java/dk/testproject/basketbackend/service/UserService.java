package dk.testproject.basketbackend.service;

import dk.testproject.basketbackend.models.User;
import dk.testproject.basketbackend.payload.request.CredentialsDto;
import dk.testproject.basketbackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

    public ResponseEntity<User> createNewUser(CredentialsDto credentialsDto) {
        return ResponseEntity.ok().body(
                this.userRepository.save(new User(
                        credentialsDto.getUsername(),
                        credentialsDto.getEmail(),
                        passwordEncoder.encode(credentialsDto.getPassword())
                ))
        );
    }


}

package dk.minkostplan.backend.service;

import dk.minkostplan.backend.entities.Role;
import dk.minkostplan.backend.entities.User;
import dk.minkostplan.backend.interfaceprojections.UserSidebarProjection;
import dk.minkostplan.backend.models.ERole;
import dk.minkostplan.backend.payload.request.RegisterRequest;
import dk.minkostplan.backend.payload.response.SidebarUserDTO;
import dk.minkostplan.backend.payload.response.UserDTO;
import dk.minkostplan.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;


    @Autowired
    public UserService(UserRepository userRepository, RoleService roleService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }

    public Optional<User> findByEmail(String email){
        return this.userRepository.getUserByEmail(email);
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

    @Transactional
    public UserDTO createNewUser(RegisterRequest registerRequest) {
        Role role = roleService.getRoleByName(ERole.ROLE_USER);

        final User user = this.userRepository.save(new User(
                registerRequest.getUsername(),
                registerRequest.getEmail(),
                passwordEncoder.encode(registerRequest.getPassword()),
                role,
                registerRequest.getActivity(),
                registerRequest.getGender(),
                registerRequest.getWeight(),
                registerRequest.getHeight(),
                registerRequest.getBirthday()));
        return new UserDTO(user);

    }

    public SidebarUserDTO getSidebarUserDTOByUserEmail(String email) {
        UserSidebarProjection userSidebarProjectionByEmail = userRepository.getUserSidebarViewByEmail(email)
                .orElseThrow(
                        () -> new UsernameNotFoundException("Kunne ikke finde brugeren!")
                );
        return new SidebarUserDTO(userSidebarProjectionByEmail);
    }
}

package dk.minkostplan.backend.payload.response;

import dk.minkostplan.backend.entities.Role;
import dk.minkostplan.backend.entities.User;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class UserDTO {
    private Long id;
    private String username;
    private String email;
    private List<String> roles;

    public UserDTO(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.roles = user.getRoles().stream().map(Role::getName).collect(Collectors.toList());
    }

    protected UserDTO() { }
}

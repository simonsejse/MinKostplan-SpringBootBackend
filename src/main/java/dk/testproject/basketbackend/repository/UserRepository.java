package dk.testproject.basketbackend.repository;

import dk.testproject.basketbackend.models.User;
import dk.testproject.basketbackend.payload.response.UserDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u from User u LEFT JOIN FETCH u.roles r WHERE u.email = :email")
    Optional<User> getUserByEmail(String email);

}

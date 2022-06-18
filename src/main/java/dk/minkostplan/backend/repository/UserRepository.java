package dk.minkostplan.backend.repository;

import dk.minkostplan.backend.entities.User;
import dk.minkostplan.backend.interfaceprojections.UserSidebarProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT u from User u JOIN FETCH u.roles WHERE u.email = :email")
    Optional<User> getUserByEmail(String email);

    @Query("SELECT u from User u WHERE u.email =:email")
    Optional<UserSidebarProjection> getUserSidebarViewByEmail(String email);

}

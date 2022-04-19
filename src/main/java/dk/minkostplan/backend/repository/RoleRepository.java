package dk.minkostplan.backend.repository;

import dk.minkostplan.backend.entities.Role;
import dk.minkostplan.backend.models.ERole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(ERole name);
    boolean existsByName(ERole eRole);
}

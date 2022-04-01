package dk.minkostplan.backend.repository;

import dk.minkostplan.backend.entities.Meta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MetaRepository extends JpaRepository<Meta, Long> {
    Optional<Meta> findByMeta(String meta);
}

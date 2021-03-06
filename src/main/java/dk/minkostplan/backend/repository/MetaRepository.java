package dk.minkostplan.backend.repository;

import dk.minkostplan.backend.entities.Ingredient;
import dk.minkostplan.backend.entities.Meta;
import dk.minkostplan.backend.entities.MetaType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

@Repository
public interface MetaRepository extends JpaRepository<Meta, Long> {
    Optional<Meta> findByMetaAndMetaType(String meta, MetaType metaType);
    boolean existsByMeta(String meta);
}

package dk.minkostplan.backend.repository;

import dk.minkostplan.backend.entities.DietPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DietPlanRepository extends JpaRepository<DietPlan, Long> {
}

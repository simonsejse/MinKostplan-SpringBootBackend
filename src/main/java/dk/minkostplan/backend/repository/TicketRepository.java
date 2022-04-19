package dk.minkostplan.backend.repository;

import dk.minkostplan.backend.entities.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
}

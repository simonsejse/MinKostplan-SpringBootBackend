package dk.minkostplan.backend.entities;

import dk.minkostplan.backend.models.TicketStatus;
import dk.minkostplan.backend.payload.request.NewTicketRequest;
import lombok.Setter;

import javax.persistence.*;

@Entity(name="Ticket")
@Table(name="tickets")
@Setter
public class Ticket {
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "ticket_seq_gen"
    )
    @SequenceGenerator(
            name="ticket_seq_gen",
            allocationSize = 1
    )
    @Id
    private Long id;

    @Column(name="description", nullable = false)
    private String description;

    @Enumerated(value=EnumType.STRING)
    @Column(name="ticket_status", nullable = false)
    private TicketStatus status;

    //many tickets can be submitted by the same user
    @ManyToOne(fetch = FetchType.LAZY)
    private User submittedBy;

    //many tickets can be completed by the same user
    @ManyToOne(fetch = FetchType.LAZY)
    private User completedBy;

    public Ticket(NewTicketRequest ticketRequest){
        this.description = ticketRequest.getDescription();
        this.status = TicketStatus.PENDING;
    }

    protected Ticket(){}
}

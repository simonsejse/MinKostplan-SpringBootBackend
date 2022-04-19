package dk.minkostplan.backend.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dk.minkostplan.backend.entities.Ticket;
import dk.minkostplan.backend.entities.User;
import dk.minkostplan.backend.payload.request.NewTicketRequest;
import dk.minkostplan.backend.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class TicketService {
    private final TicketRepository ticketRepository;
    private final UserService userService;
    private final ObjectMapper mapper;

    @Autowired
    public TicketService(TicketRepository ticketRepository, UserService userService, ObjectMapper mapper) {
        this.ticketRepository = ticketRepository;
        this.userService = userService;
        this.mapper = mapper;
    }

    public void createNewTicket(NewTicketRequest newTicketRequest, String userName) throws UsernameNotFoundException, JsonProcessingException {
        User userByUsername = userService.getUserByUsername(userName);
        Ticket ticket = new Ticket(newTicketRequest);

        userByUsername.createNewTicket(ticket);
    }
}

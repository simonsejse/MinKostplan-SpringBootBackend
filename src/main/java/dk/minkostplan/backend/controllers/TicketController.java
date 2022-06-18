package dk.minkostplan.backend.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import dk.minkostplan.backend.payload.request.NewTicketRequest;
import dk.minkostplan.backend.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("api/tickets")
public class TicketController {

    private final TicketService ticketService;

    @Autowired
    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }


    @PostMapping("/new")
    public ResponseEntity<String> createNewTicket(
            @RequestBody @Valid NewTicketRequest newTicketRequest, Authentication authentication
    ) throws JsonProcessingException, UsernameNotFoundException {
        String name = authentication.getName();
        ticketService.createNewTicket(newTicketRequest, name);
        return ResponseEntity.ok("OK");
    }

}

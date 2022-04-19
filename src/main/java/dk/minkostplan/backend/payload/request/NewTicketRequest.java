package dk.minkostplan.backend.payload.request;

import dk.minkostplan.backend.models.TicketStatus;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class NewTicketRequest {
    @NotNull(message = "Beskrivelsen kan ikke være tom!")
    @NotBlank(message = "Beskrivelsen kan ikke være blank!")
    private String description;
}

package dk.minkostplan.backend.payload.request.recipe;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class InstructionCreateRequest {
    @NotNull(message = "Din instruktioner har fået et forkert nummer, slet og prøv igen!")
    private Integer number;
    @NotNull(message = "En af dine instruktioner er blanke!")
    @NotBlank(message = "En af dine instruktioner er blanke!")
    @Size(min=10, max = 200, message = "Dine instruktion skal være mellem 10-200 karakterer.")
    private String step;
}

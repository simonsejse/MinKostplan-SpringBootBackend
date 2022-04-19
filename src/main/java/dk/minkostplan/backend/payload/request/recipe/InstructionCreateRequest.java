package dk.minkostplan.backend.payload.request.recipe;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class InstructionCreateRequest {
    @Min(value = 1, message = "Din instruktion kan ikke være lavere end 1.")
    private Integer number;
    @NotNull(message = "Din instruks kan ikke være null!")
    @Size(min=10, max = 200, message = "Din instruks skal være mellem 10-200 characters.")
    private String step;
}

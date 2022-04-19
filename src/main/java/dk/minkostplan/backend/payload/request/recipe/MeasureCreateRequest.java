package dk.minkostplan.backend.payload.request.recipe;

import dk.minkostplan.backend.models.MeasureType;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

@Data
public class MeasureCreateRequest {
    @NotNull(message = "type feltet kan ikke være tomt!")
    private MeasureType type;
    @NotNull(message = "amountOfType feltet kan ikke være tomt!")
    @PositiveOrZero(message = "Mængden af din egen givne måleenhed kan ikke være et negativ tal!")
    private Float amountOfType;
    @NotNull(message = "amountInGrams feltet kan ikke være tomt!")
    @PositiveOrZero(message = "Mængden af gram i din ingrediens kan ikke være negativ!")
    private Float amountInGrams;
}

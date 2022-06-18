package dk.minkostplan.backend.payload.request.recipe;

import dk.minkostplan.backend.constraints.MetaExistOrCreate;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;
import java.util.Set;

@Data
public class IngredientCreateRequest {
    @NotNull(message = "foodId feltet kan ikke være tomt!")
    @PositiveOrZero(message = "Der er intet food ID under 0!")
    private Integer foodId;
    @NotNull(message = "amount feltet kan ikke være tomt!")
    @PositiveOrZero(message = "Du kan ikke vælge en mængde under 0!")
    private Float amount;
    @NotNull(message = "Dit measure kan ikke være null")
    private @Valid MeasureCreateRequest measures;
}

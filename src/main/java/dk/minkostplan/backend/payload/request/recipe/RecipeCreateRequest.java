package dk.minkostplan.backend.payload.request.recipe;

import dk.minkostplan.backend.constraints.RecipeTypeValid;
import dk.minkostplan.backend.models.RecipeType;
import lombok.Data;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
public class RecipeCreateRequest {
    @Size(min = 3, max = 72, message = "Navn på din ret kan være mellem 3-72 characters.")
    @NotBlank(message = "Navnet på din ret kan ikke være blank!")
    @NotNull(message = "Du mangler navn feltet!")
    private String name;
    @Size(min = 20, max = 300, message = "Din beskrivelse skal mindst være mellem 20-300 ord.")
    @NotBlank(message = "Din beskrivelse kan ikke være blank!")
    @NotNull(message = "Du mangler beskrivelse feltet!")
    private String description;
    @NotBlank(message = "Du kan ikke lade opskrift typen være tom!")
    @NotNull(message = "Du mangler rettens type feltet!")
    @RecipeTypeValid
    private String type;
    @NotNull(message = "Du mangler vegetarisk feltet!")
    private Boolean vegetarian;
    @NotNull(message = "Du mangler vegansk feltet!")
    private Boolean vegan;
    @NotNull(message = "Du mangler glutenfri feltet!")
    private Boolean glutenFree;
    @NotNull(message = "Du mangler mælkefri feltet!")
    private Boolean dairyFree;
    @NotNull(message = "Du mangler 'meget sundt' feltet!")
    private Boolean veryHealthy;
    @NotNull(message = "Du mangler 'billigt' feltet!")
    private Boolean cheap;
    @NotNull(message = "Du mangler 'er meget populært' feltet!")
    private Boolean veryPopular;
    @NotNull(message = "Du mangler bæredygtigt feltet!")
    private Boolean sustainable;
    @NotNull(message = "Du mangler pris pr servering feltet!")
    private Float pricePerServing;
    @NotNull(message = "Du mangler instruktioner feltet!")
    private String instructions;
    @NotNull(message = "Du mangler (KLAR OM (MIN.) feltet!")
    private Integer readyInMinutes;
    @NotNull(message = "Du mangler billede feltet!")
    @NotBlank(message = "Du mangler billede feltet!")
    private String image;
    @NotEmpty(message = "Du mangler at tilføje ingrediserne!")
    @NotNull(message = "Du mangler at tilføje ingrediserne!")
    private List<@Valid IngredientCreateRequest> ingredients;
    @NotEmpty(message = "Du mangler at tilføje instruktioner!")
    @NotNull(message = "Du mangler at tilføje instruktioner!")
    private List<@Valid InstructionCreateRequest> analyzedInstructions;
}

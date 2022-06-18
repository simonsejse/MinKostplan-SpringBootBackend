package dk.minkostplan.backend.payload.request.recipe;

import dk.minkostplan.backend.constraints.EnumNamePattern;
import dk.minkostplan.backend.constraints.MetaExistOrCreate;
import dk.minkostplan.backend.models.RecipeType;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Set;

@Data
public class RecipeCreateRequest {
    @Size(min = 5, max = 100, message = "Navn på din ret kan være mellem 5-100 characters.")
    @NotBlank(message = "Navnet på din ret kan ikke være blank!")
    @NotNull(message = "Du mangler navn feltet!")
    private String name;
    @Size(min = 20, max = 500, message = "Din beskrivelse skal mindst være mellem 20-500 ord.")
    @NotBlank(message = "Din beskrivelse kan ikke være blank!")
    @NotNull(message = "Du mangler beskrivelse feltet!")
    private String description;
    @NotNull(message = "Du mangler rettens type feltet!")
    @EnumNamePattern(
        enumNames ="MORGENMAD|EFTERMIDDAGSMAD|FROKOST|SNACK|AFTENSMAD|ALLE",
        message="Denne opskrift type findes ikke!"
    )
    private RecipeType type;
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
    @NotNull(message = "metas feltet kan ikke være tomt!")
    @MetaExistOrCreate
    private Set<String> meta;
    @NotEmpty(message = "Du mangler at tilføje ingredienserne!")
    @NotNull(message = "Du mangler at tilføje ingredienserne!")
    private List<@Valid IngredientCreateRequest> ingredients;
    @NotEmpty(message = "Du mangler at tilføje instruktioner!")
    @NotNull(message = "Du mangler at tilføje instruktioner!")
    private List<@Valid InstructionCreateRequest> analyzedInstructions;
}

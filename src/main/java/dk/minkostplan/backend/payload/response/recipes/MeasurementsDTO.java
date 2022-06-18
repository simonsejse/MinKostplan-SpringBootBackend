package dk.minkostplan.backend.payload.response.recipes;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class MeasurementsDTO {
	@JsonProperty(value="id")
	private Long id;
	@JsonProperty(value="type")
	private String type;
	@JsonProperty(value="amountOfType")
	private float amountOfType;
	@JsonProperty(value="amountInGrams")
	private float amountInGrams;
}

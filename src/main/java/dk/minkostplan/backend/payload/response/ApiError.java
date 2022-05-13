package dk.minkostplan.backend.payload.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ApiError{
    @JsonProperty("status")
    private int status;
    @JsonProperty("timestamp")
    private long timestamp;
    @JsonProperty("errorType")
    private String errorType;
    @JsonProperty("errors")
    private List<String> errors;
    @JsonProperty("path")
    private String path;
}

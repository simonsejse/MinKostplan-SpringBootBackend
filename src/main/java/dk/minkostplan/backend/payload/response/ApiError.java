package dk.minkostplan.backend.payload.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ApiError extends Object{
    @JsonProperty("status")
    private int status;
    @JsonProperty("timestamp")
    private long timestamp;
    @JsonProperty("errorType")
    private String errorType;
    @JsonProperty("message")
    private String message;
    @JsonProperty("path")
    private String path;
}

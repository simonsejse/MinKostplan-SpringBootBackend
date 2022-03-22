package dk.minkostplan.backend.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Date;

@AllArgsConstructor
@Data
@Getter
@Setter
public class ErrorDTO {
    private Date timestamp;
    private String message;
    private HttpStatus statusCode;

}

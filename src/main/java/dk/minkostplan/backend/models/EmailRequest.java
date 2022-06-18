package dk.minkostplan.backend.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
public class EmailRequest {
    @Getter
    @Setter
    private String to;
    @Getter
    @Setter
    private String subject;
    @Getter
    @Setter
    private String body;
}

package dk.minkostplan.backend.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
public class PwdResetTokenView implements Serializable {
    @Getter
    private final UUID token;
    @Getter
    private final  LocalDateTime expiresAt;
    @Getter
    private final String email;
}



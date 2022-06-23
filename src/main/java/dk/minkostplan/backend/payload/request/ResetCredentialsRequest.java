package dk.minkostplan.backend.payload.request;

import lombok.Data;

@Data
public class ResetCredentialsRequest{
    private String token;
    private String newPassword;
    private String newPasswordConfirmed;
}
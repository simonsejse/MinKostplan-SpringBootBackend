package dk.minkostplan.backend.exceptions;

import org.springframework.http.HttpStatus;

public class ResetCredentialsException extends RuntimeException {
    private final HttpStatus status;
    public ResetCredentialsException(String error, HttpStatus status){
        super(error);
        this.status = status;
    }
    public HttpStatus getStatus() {
        return status;
    }
}

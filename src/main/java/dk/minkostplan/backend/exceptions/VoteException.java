package dk.minkostplan.backend.exceptions;

import org.springframework.http.HttpStatus;

public class VoteException extends RuntimeException {
    private HttpStatus status;

    public VoteException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}

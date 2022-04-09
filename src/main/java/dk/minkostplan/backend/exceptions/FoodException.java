package dk.minkostplan.backend.exceptions;

import org.springframework.http.HttpStatus;

public class FoodException extends Exception {
    private final HttpStatus status;

    public FoodException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}

package dk.minkostplan.backend.exceptions;

import org.springframework.http.HttpStatus;

public class MetaException extends Exception{
    private final HttpStatus status;

    public MetaException(HttpStatus status, String message){
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}

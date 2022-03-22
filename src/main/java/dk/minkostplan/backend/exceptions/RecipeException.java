package dk.minkostplan.backend.exceptions;

import org.springframework.http.HttpStatus;

public class RecipeException extends Exception {

    private HttpStatus status;

    public RecipeException(String message, HttpStatus status){
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}



package dk.testproject.basketbackend.controllers;


import dk.testproject.basketbackend.payload.response.ErrorDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Date;

@ControllerAdvice
public class ExceptionHandlingController {
    public ResponseEntity<ErrorDTO> createResponseEntity(HttpStatus status, String message){
        return ResponseEntity.status(status).body(new ErrorDTO(new Date(), message, status));
    }
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorDTO> runTimeException(RuntimeException e){
        return createResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }
}

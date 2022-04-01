package dk.minkostplan.backend.controllers;


import dk.minkostplan.backend.exceptions.MetaException;
import dk.minkostplan.backend.exceptions.RecipeException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ExceptionHandlingController {

    public ResponseEntity<Map<String, Object>> createResponseEntity(HttpStatus status, String message, final HttpServletRequest request){
        final Map<String, Object> body = new HashMap<>();
        body.put("status", status.value());
        body.put("timestamp", LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));
        body.put("error",  status.name());
        body.put("message", message);
        body.put("path", request.getServletPath());
        return ResponseEntity.status(status).body(body);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<Map<String, Object>> usernameNotFound(final HttpServletRequest request, UsernameNotFoundException e){
        return createResponseEntity(HttpStatus.NOT_FOUND, e.getMessage(), request);
    }

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ResponseEntity<Map<String, Object>> SQLIntegrityConstraintViolationException(final HttpServletRequest request, SQLIntegrityConstraintViolationException e){
        return createResponseEntity(HttpStatus.CONFLICT, e.getMessage(), request);
    }

    @ExceptionHandler(RecipeException.class)
    public ResponseEntity<Map<String, Object>> mealException(final HttpServletRequest request, RecipeException exception){
        return createResponseEntity(exception.getStatus(), exception.getMessage(), request);
    }

    @ExceptionHandler(MetaException.class)
    public ResponseEntity<Map<String, Object>> metaException(final HttpServletRequest request, MetaException exception){
        return createResponseEntity(exception.getStatus(), exception.getMessage(), request);
    }

}
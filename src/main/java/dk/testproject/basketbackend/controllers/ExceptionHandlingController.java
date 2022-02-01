package dk.testproject.basketbackend.controllers;


import dk.testproject.basketbackend.payload.response.ErrorDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ExceptionHandlingController {
    /**
     *
     *   body.put("status", HttpServletResponse.SC_BAD_REQUEST);
*         body.put("error", "Unauthorized");
*         body.put("message", exception.getMessage());
*         body.put("path", request.getServletPath());
     */

    public ResponseEntity<Map<String, Object>> createResponseEntity(HttpStatus status, String message, final HttpServletRequest request){
        final Map<String, Object> body = new HashMap<>();
        body.put("status", status.value());
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
    public ResponseEntity<Map<String, Object>> sQLIntegrityConstraintViolationException(final HttpServletRequest request, SQLIntegrityConstraintViolationException e){
        return createResponseEntity(HttpStatus.CONFLICT, e.getMessage(), request);
    }


}

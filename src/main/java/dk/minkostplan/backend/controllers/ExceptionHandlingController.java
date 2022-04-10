package dk.minkostplan.backend.controllers;


import dk.minkostplan.backend.exceptions.FoodException;
import dk.minkostplan.backend.exceptions.MetaException;
import dk.minkostplan.backend.exceptions.RecipeException;
import dk.minkostplan.backend.payload.response.ApiError;
import org.springframework.beans.TypeMismatchException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class ExceptionHandlingController extends ResponseEntityExceptionHandler {

    public ResponseEntity<Object> createResponseEntity(HttpStatus status, String message, String path){
        ApiError apiError = ApiError.builder()
                .path(path)
                .timestamp(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC))
                .message(message)
                .status(status.value())
                .errorType(status.name())
                .build();
        return ResponseEntity.status(status).body(apiError);
    }


    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        FieldError fieldError = ex.getFieldError();
        String errorMessage = fieldError != null ? fieldError.getDefaultMessage() : ex.getMessage();
        return createResponseEntity(status, errorMessage, "");
    }


    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return createResponseEntity(status, ex.getMessage(), "");
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return createResponseEntity(status, ex.getMessage(), request.getContextPath());
    }


    @Override
    protected ResponseEntity<Object> handleBindException(BindException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        FieldError fieldError = ex.getFieldError();
        logger.info(ex.getMessage());
        String errorMessage = fieldError != null ? fieldError.getDefaultMessage() : ex.getMessage();
        return createResponseEntity(status, errorMessage, request.getContextPath());
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<Object> handleUsernameNotFound(final HttpServletRequest request, UsernameNotFoundException e){
        return createResponseEntity(HttpStatus.NOT_FOUND, e.getMessage(), request.getServletPath());
    }

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ResponseEntity<Object> handleSQLIntegrityConstraintViolationException(final HttpServletRequest request, SQLIntegrityConstraintViolationException e){
        return createResponseEntity(HttpStatus.CONFLICT, e.getMessage(), request.getServletPath());
    }

    @ExceptionHandler(FoodException.class)
    public ResponseEntity<Object> handleFoodException(final HttpServletRequest request, FoodException exception){
        return createResponseEntity(exception.getStatus(), exception.getMessage(), request.getServletPath());
    }

    @ExceptionHandler(RecipeException.class)
    public ResponseEntity<Object> handleMealException(final HttpServletRequest request, RecipeException exception){
        return createResponseEntity(exception.getStatus(), exception.getMessage(), request.getServletPath());
    }

    @ExceptionHandler(MetaException.class)
    public ResponseEntity<Object> handleMetaException(final HttpServletRequest request, MetaException exception){
        return createResponseEntity(exception.getStatus(), exception.getMessage(), request.getServletPath());
    }
}
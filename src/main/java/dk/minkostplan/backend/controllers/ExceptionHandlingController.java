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
import org.springframework.validation.ObjectError;
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
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class ExceptionHandlingController extends ResponseEntityExceptionHandler {

    public ResponseEntity<Object> createResponseEntity(HttpStatus status, List<String> errors, String path){
        ApiError apiError = ApiError.builder()
                .path(path)
                .timestamp(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC))
                .errors(errors)
                .status(status.value())
                .errorType(status.name())
                .build();
        return ResponseEntity.status(status).body(apiError);
    }


    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        List<String> errors = new ArrayList<>();
        ex.getAllErrors().stream().map(ObjectError::getDefaultMessage).forEach(errors::add);
        return createResponseEntity(status, errors, "");
    }


    @Override
    protected ResponseEntity<Object> handleBindException(BindException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        List<String> errors = new ArrayList<>();
        ex.getFieldErrors().stream().map(FieldError::getDefaultMessage).forEach(errors::add);
        return createResponseEntity(status, errors, request.getContextPath());
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return createResponseEntity(status, Collections.singletonList(ex.getMessage()), "");
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return createResponseEntity(status, Collections.singletonList(ex.getMessage()), request.getContextPath());
    }


    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<Object> handleUsernameNotFound(final HttpServletRequest request, UsernameNotFoundException e){
        return createResponseEntity(HttpStatus.NOT_FOUND, Collections.singletonList(e.getMessage()), request.getServletPath());
    }

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ResponseEntity<Object> handleSQLIntegrityConstraintViolationException(final HttpServletRequest request, SQLIntegrityConstraintViolationException e){
        return createResponseEntity(HttpStatus.CONFLICT, Collections.singletonList(e.getMessage()), request.getServletPath());
    }

    @ExceptionHandler(FoodException.class)
    public ResponseEntity<Object> handleFoodException(final HttpServletRequest request, FoodException exception){
        return createResponseEntity(exception.getStatus(), Collections.singletonList(exception.getMessage()), request.getServletPath());
    }

    @ExceptionHandler(RecipeException.class)
    public ResponseEntity<Object> handleMealException(final HttpServletRequest request, RecipeException exception){
        return createResponseEntity(exception.getStatus(), Collections.singletonList(exception.getMessage()), request.getServletPath());
    }

    @ExceptionHandler(MetaException.class)
    public ResponseEntity<Object> handleMetaException(final HttpServletRequest request, MetaException exception){
        return createResponseEntity(exception.getStatus(), Collections.singletonList(exception.getMessage()), request.getServletPath());
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Object> handleIllegalStateException(final HttpServletRequest request, IllegalStateException exception){

        /* change illegal state "httpstatus.bad_request" not sure if its right status code */
        return createResponseEntity(HttpStatus.BAD_REQUEST, Collections.singletonList(exception.getMessage()), request.getServletPath());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintException(final HttpServletRequest request, ConstraintViolationException exception){
        final List<ConstraintViolation<?>> constraintViolations = new ArrayList<>(exception.getConstraintViolations());
        return createResponseEntity(HttpStatus.BAD_REQUEST, Collections.singletonList(constraintViolations.get(0).getMessage()), request.getServletPath());
    }
}
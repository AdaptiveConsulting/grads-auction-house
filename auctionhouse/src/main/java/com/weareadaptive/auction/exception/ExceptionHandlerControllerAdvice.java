package com.weareadaptive.auction.exception;

import com.weareadaptive.auction.model.BusinessException;
import com.weareadaptive.auction.model.NoContentResponseException;
import com.weareadaptive.auction.model.ObjectNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.APPLICATION_PROBLEM_JSON;

@ControllerAdvice
public class ExceptionHandlerControllerAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException ex) {
        var headers = new HttpHeaders();
        headers.setContentType(APPLICATION_PROBLEM_JSON);

        var invalidFields = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> new InvalidField(error.getField(), error.getDefaultMessage()))
                .toList();

        return new ResponseEntity<>(new BadRequestInvalidFieldsProblem(invalidFields), headers,
                BAD_REQUEST);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Object> handleBusinessException(
            BusinessException ex) {
        var headers = new HttpHeaders();
        headers.setContentType(APPLICATION_PROBLEM_JSON);
        return new ResponseEntity<>(
                new Problem(
                        BAD_REQUEST.value(),
                        BAD_REQUEST.name(),
                        ex.getMessage()),
                headers,
                BAD_REQUEST);
    }

    @ExceptionHandler(ObjectNotFoundException.class)
    public ResponseEntity<Object> handleNotFoundException() {
        var headers = new HttpHeaders();
        headers.setContentType(APPLICATION_PROBLEM_JSON);
        return new ResponseEntity<>(headers, NOT_FOUND);
    }

    @ExceptionHandler(NoContentResponseException.class)
    public ResponseEntity<Object> handleNoContentResponseException() {
        var headers = new HttpHeaders();
        headers.setContentType(APPLICATION_PROBLEM_JSON);
        return new ResponseEntity<>(headers, NO_CONTENT);
    }
}

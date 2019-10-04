package com.primeholding.coenso.controller;

import com.primeholding.coenso.exception.DataConflictException;
import com.primeholding.coenso.exception.DuplicateEmailException;
import com.primeholding.coenso.exception.TemplateFormNotFoundException;
import com.primeholding.coenso.exception.WrongCredentialsException;
import com.primeholding.coenso.model.EmailInvalidModel;
import com.primeholding.coenso.model.ExceptionMessageModel;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class ResponseEntityExceptionHandlerImpl extends ResponseEntityExceptionHandler {
    @ExceptionHandler(value = DuplicateEmailException.class)
    public HttpEntity<EmailInvalidModel> exception(DuplicateEmailException exception) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new EmailInvalidModel(exception.getMessage(), false));
    }

    @ExceptionHandler(value = WrongCredentialsException.class)
    public HttpEntity<ExceptionMessageModel> exception(WrongCredentialsException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ExceptionMessageModel(exception.getMessage()));
    }

    @ExceptionHandler(value = DataConflictException.class)
    public HttpEntity<ExceptionMessageModel> exception(DataConflictException exception) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ExceptionMessageModel(exception.getMessage()));
    }



    @ExceptionHandler(value = TemplateFormNotFoundException.class)
    public HttpEntity<ExceptionMessageModel> exception(TemplateFormNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ExceptionMessageModel(exception.getMessage()));
    }

    @Override
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatus status, WebRequest request
    ) {
        final List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        List<String> messages = fieldErrors.stream()
                .map(error -> error.getField() + " " + error.getDefaultMessage()).collect(Collectors.toList());
        Map<String,String> errorMessages = new HashMap<>();
        for(int i = 0; i < messages.size(); i++) {
            errorMessages.put("message " + i, messages.get(i));
        }
        return new ResponseEntity<>(errorMessages, headers, status);
    }
}

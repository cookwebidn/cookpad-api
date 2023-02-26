package com.cookpadidn.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    private static final String DESCRIPTION = "check for valid data";

    @ExceptionHandler({ValidationException.class})
    public ResponseEntity<ErrorResponse> validationException(ValidationException exception, HttpServletRequest request) {
        log.error("validation exception : {} for {}", exception.getLocalizedMessage(), request.getRequestURI());

        return new ResponseEntity<>(
                ErrorResponse.builder()
                        .timestamp(new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date()))
                        .status(BAD_REQUEST.value())
                        .error(BAD_REQUEST.name())
                        .message(new HashMap<>())
                        .path(request.getRequestURI())
                        .request(request.getMethod())
                        .description(exception.getLocalizedMessage())
                        .build(), BAD_REQUEST
        );
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<ErrorResponse> genericException(
            Exception exception,
            HttpServletRequest httpServletRequest
    ) {
        log.error("exception : {} for {}", exception.getLocalizedMessage(), httpServletRequest.getRequestURI());

        return new ResponseEntity<>(
                ErrorResponse.builder()
                        .timestamp(new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date()))
                        .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .error(HttpStatus.INTERNAL_SERVER_ERROR.name())
                        .message(new HashMap<>())
                        .path(httpServletRequest.getRequestURI())
                        .request(httpServletRequest.getMethod())
                        .description(exception.getLocalizedMessage())
                        .build(), HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> invalidArgumentException(MethodArgumentNotValidException ex, HttpServletRequest request){
        Map<String, String> errorMessage = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> errorMessage.put(error.getField(), error.getDefaultMessage()));
        return new ResponseEntity<>(
                ErrorResponse.builder()
                        .timestamp(new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date()))
                        .status(BAD_REQUEST.value())
                        .error(BAD_REQUEST.name())
                        .message(errorMessage)
                        .path(request.getRequestURI())
                        .request(request.getMethod())
                        .description(DESCRIPTION)
                        .build(), BAD_REQUEST
        );
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> badRequestException(BadRequestException ex, HttpServletRequest request){
        return new ResponseEntity<>(
                ErrorResponse.builder()
                        .timestamp(new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date()))
                        .status(BAD_REQUEST.value())
                        .error(BAD_REQUEST.name())
                        .message(ex.getErrorMessage())
                        .path(request.getRequestURI())
                        .request(request.getMethod())
                        .description(DESCRIPTION)
                        .build(), BAD_REQUEST
        );
    }

    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> resourceNotFoundException(ResourceNotFoundException ex, HttpServletRequest request){
        String logFormat = String.format("%s not found with %s: '%s'", ex.getResourceName(), ex.getFieldName(), ex.getFieldValue());
        log.error(logFormat);
        return new ResponseEntity<>(
                ErrorResponse.builder()
                        .timestamp(new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date()))
                        .status(NOT_FOUND.value())
                        .error(NOT_FOUND.name())
                        .message(ex.getMessageMap())
                        .path(request.getRequestURI())
                        .request(request.getMethod())
                        .description(DESCRIPTION)
                        .build(), NOT_FOUND
        );
    }

}

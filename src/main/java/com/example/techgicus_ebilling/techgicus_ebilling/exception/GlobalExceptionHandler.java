package com.example.techgicus_ebilling.techgicus_ebilling.exception;


import com.example.techgicus_ebilling.techgicus_ebilling.dto.errorDto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourseNotFoundException(ResourceNotFoundException exception){
        return buildResponse(HttpStatus.NOT_FOUND,exception.getMessage());
    }

    @ExceptionHandler(UserAlreadyRegisterException.class)
    public ResponseEntity<ErrorResponse> handleUserAlreadyRegisterException(UserAlreadyRegisterException exception){
        return buildResponse(HttpStatus.CONFLICT,exception.getMessage());
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<ErrorResponse> handleInvalidTokenException(InvalidTokenException exception){
        return buildResponse(exception.getStatus(),exception.getMessage());
    }

    @ExceptionHandler(UserSubscriptionNotActiveException.class)
    public ResponseEntity<ErrorResponse> handleUserSubscriptionNotActiveException(UserSubscriptionNotActiveException exception){
        return buildResponse(HttpStatus.NOT_FOUND,exception.getMessage());
    }

    public ResponseEntity<ErrorResponse> buildResponse(HttpStatus httpStatus, String message){
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setTimestamp(LocalDateTime.now());
        errorResponse.setStatus(httpStatus.value());
        errorResponse.setError(httpStatus.getReasonPhrase());
        errorResponse.setMessage(message);

        return new ResponseEntity<>(errorResponse,httpStatus);
    }
}

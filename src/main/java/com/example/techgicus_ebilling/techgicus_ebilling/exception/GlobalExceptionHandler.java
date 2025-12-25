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


    @ExceptionHandler(InvalidPaymentAmountException.class)
    public ResponseEntity<ErrorResponse> handleInvalidPaymentAmountException(InvalidPaymentAmountException exception){
        return buildResponse(HttpStatus.BAD_REQUEST,exception.getMessage());
    }


    @ExceptionHandler(QuotationAlreadyClosedException.class)
    public ResponseEntity<ErrorResponse> handleQuotationAlreadyClosedException(QuotationAlreadyClosedException exception){
        return buildResponse(HttpStatus.BAD_REQUEST,exception.getMessage());
    }

    @ExceptionHandler(SaleOrderAlreadyClosedException.class)
    public ResponseEntity<ErrorResponse> handleSaleOrderAlreadyClosedException(SaleOrderAlreadyClosedException exception){
        return buildResponse(HttpStatus.BAD_REQUEST,exception.getMessage());
    }

    @ExceptionHandler(PurchaseOrderAlreadyClosedException.class)
    public ResponseEntity<ErrorResponse> handlePurchaseOrderAlreadyClosedException(PurchaseOrderAlreadyClosedException exception){
        return buildResponse(HttpStatus.BAD_REQUEST,exception.getMessage());
    }

    @ExceptionHandler(DeliveryChallanAlreadyClosedException.class)
    public ResponseEntity<ErrorResponse> handleDeliveryChallanAlreadyClosedException(DeliveryChallanAlreadyClosedException exception){
        return buildResponse(HttpStatus.BAD_REQUEST,exception.getMessage());
    }


    @ExceptionHandler(DashboardException.class)
    public ResponseEntity<ErrorResponse> handleDashboardException(DashboardException exception){
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR,exception.getMessage());
    }

    @ExceptionHandler(PaymentProcessingException.class)
    public ResponseEntity<ErrorResponse> handlePaymentProcessingException(PaymentProcessingException exception){
        return buildResponse(HttpStatus.BAD_REQUEST,exception.getMessage());
    }

    @ExceptionHandler(ItemAlreadyExitException.class)
    public ResponseEntity<ErrorResponse> handleItemAlreadyExitException(ItemAlreadyExitException exception){
        return buildResponse(HttpStatus.CONFLICT,exception.getMessage());
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequestException(BadRequestException exception){
        return buildResponse(HttpStatus.BAD_REQUEST,exception.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgument(IllegalArgumentException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return ResponseEntity.badRequest().body(error);
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

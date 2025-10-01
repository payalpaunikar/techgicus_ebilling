package com.example.techgicus_ebilling.techgicus_ebilling.dto.errorDto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "Error response returned when a request fails")
public class ErrorResponse {

    @Schema(description = "Timestamp when the error occurred", example = "2025-08-29T13:26:56.2382162")
    private LocalDateTime timestamp;

    @Schema(description = "HTTP status code", example = "error status like : 409, 401,403")
    private int status;

    @Schema(description = "Error type", example = "error status like : Conflict,Unauthorized")
    private String error;

    @Schema(description = "Detailed error message", example = "error message")
    private String message;

    public ErrorResponse() {
    }

    public ErrorResponse(LocalDateTime timestamp, int status, String error, String message) {
        this.timestamp = timestamp;
        this.status = status;
        this.error = error;
        this.message = message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

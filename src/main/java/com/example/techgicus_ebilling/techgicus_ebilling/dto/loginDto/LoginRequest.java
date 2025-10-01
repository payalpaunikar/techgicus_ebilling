package com.example.techgicus_ebilling.techgicus_ebilling.dto.loginDto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;


@Schema
public class LoginRequest {

    @NotBlank(message = "email can not be null or blank.")
    @Email(message = "provide valid email.")
    @Schema(example = "payalpaunikar1001@gmail.com")
    private String emailId;

    @NotBlank(message = "password can not be null or blank.")
    @Schema(example = "Admin@123")
    private String password;

    public LoginRequest() {
    }

    public LoginRequest(String emailId, String password) {
        this.emailId = emailId;
        this.password = password;
    }

    public @NotBlank(message = "email can not be null or blank.") @Email(message = "provide valid email.") String getEmailId() {
        return emailId;
    }

    public void setEmailId(@NotBlank(message = "email can not be null or blank.") @Email(message = "provide valid email.") String emailId) {
        this.emailId = emailId;
    }

    public @NotBlank(message = "password can not be null or blank.") String getPassword() {
        return password;
    }

    public void setPassword(@NotBlank(message = "password can not be null or blank.") String password) {
        this.password = password;
    }
}

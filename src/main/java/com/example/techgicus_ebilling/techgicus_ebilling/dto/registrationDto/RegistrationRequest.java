package com.example.techgicus_ebilling.techgicus_ebilling.dto.registrationDto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;


@Schema
public class RegistrationRequest {

    @NotBlank(message = "username can not be null or blank.")
    @Schema(example = "Payal Paunikar")
    private String userName;

    @NotBlank(message = "email can not be null or blank.")
    @Email(message = "provide valid email.")
    @Schema(example = "payalpaunikar1001@gmail.com")
    private String emailId;

    @NotBlank(message = "password can not be null or blank.")
    @Schema(example = "Payal@123")
    private String password;

    public RegistrationRequest() {
    }

    public RegistrationRequest(String userName, String emailId, String password) {
        this.userName = userName;
        this.emailId = emailId;
        this.password = password;
    }

    public @NotBlank(message = "username can not be null or blank.") String getUserName() {
        return userName;
    }

    public void setUserName(@NotBlank(message = "username can not be null or blank.") String userName) {
        this.userName = userName;
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

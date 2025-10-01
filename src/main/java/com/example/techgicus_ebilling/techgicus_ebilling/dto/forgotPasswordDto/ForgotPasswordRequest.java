package com.example.techgicus_ebilling.techgicus_ebilling.dto.forgotPasswordDto;


import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "forgot password request")
public class ForgotPasswordRequest {

    @Schema(example = "payalpaunikar1001@gmail.com")
    private String emailId;

    @Schema(example = "Payal@123")
    private String newPassword;

    public ForgotPasswordRequest() {
    }

    public ForgotPasswordRequest(String emailId, String newPassword) {
        this.emailId = emailId;
        this.newPassword = newPassword;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}

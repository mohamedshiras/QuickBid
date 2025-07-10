// ResetOtpResponse.java
package com.example.QuickBid.dto;

public class OtpResponse {
    private String message;
    private boolean success;

    public OtpResponse(String message, boolean success) {
        this.message = message;
        this.success = success;
    }

    // Getters and setters
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
package com.uth.quizclear.model.dto;

public class LoginResponseDTO {
    private boolean success;
    private String message;
    private String redirectUrl;
    private UserBasicDTO user;

    // Constructors
    public LoginResponseDTO() {
    }

    public LoginResponseDTO(boolean success, String message, String redirectUrl, UserBasicDTO user) {
        this.success = success;
        this.message = message;
        this.redirectUrl = redirectUrl;
        this.user = user;
    }

    // Static factory methods
    public static LoginResponseDTO success(String redirectUrl, UserBasicDTO user) {
        return new LoginResponseDTO(true, "Login successful", redirectUrl, user);
    }

    public static LoginResponseDTO failure(String message) {
        return new LoginResponseDTO(false, message, null, null);
    }

    // Getters and Setters
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    public UserBasicDTO getUser() {
        return user;
    }

    public void setUser(UserBasicDTO user) {
        this.user = user;
    }
}

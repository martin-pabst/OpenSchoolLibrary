package de.sp.modules.user.servlets;

public class ChangePasswordResponse {

    private String status;
    private String message;

    public ChangePasswordResponse(String status, String message) {
        this.status = status;
        this.message = message;
    }
}

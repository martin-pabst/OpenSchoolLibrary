package de.sp.modules.admin.servlets;

/**
 * Created by martin on 04.05.2017.
 */
public class RemoveUserResponse {

    public String status; // "error" or "success"
    public String message; // if status == "error"

    public RemoveUserResponse(String status, String message) {
        this.status = status;
        this.message = message;
    }
}

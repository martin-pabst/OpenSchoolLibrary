package de.sp.modules.admin.servlets.useradministration;

/**
 * Created by martin on 04.05.2017.
 */
public class SaveUserResponse {

    public String status; // "error" or "success"
    public String message; // if status == "error"

    public SaveUserRecord record;

    public SaveUserResponse(String status, String message, SaveUserRecord record) {
        this.status = status;
        this.message = message;
        this.record = record;
    }
}

package de.sp.modules.root.schooladministration;

/**
 * Created by martin on 04.05.2017.
 */
public class SaveAdminResponse {

    public String status; // "error" or "success"
    public String message; // if status == "error"

    public AdminData record;

    public SaveAdminResponse(String status, String message, AdminData record) {
        this.status = status;
        this.message = message;
        this.record = record;
    }
}

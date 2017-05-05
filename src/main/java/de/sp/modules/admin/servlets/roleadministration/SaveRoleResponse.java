package de.sp.modules.admin.servlets.roleadministration;

/**
 * Created by martin on 04.05.2017.
 */
public class SaveRoleResponse {

    public String status; // "error" or "success"
    public String message; // if status == "error"

    public SaveRoleRecord record;

    public SaveRoleResponse(String status, String message, SaveRoleRecord record) {
        this.status = status;
        this.message = message;
        this.record = record;
    }
}

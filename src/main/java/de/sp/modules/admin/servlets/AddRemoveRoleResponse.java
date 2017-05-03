package de.sp.modules.admin.servlets;

/**
 * Created by Martin on 03.05.2017.
 */
public class AddRemoveRoleResponse {

    private String status;
    private String message;

    public AddRemoveRoleResponse(String status, String message) {
        this.status = status;
        this.message = message;
    }
}

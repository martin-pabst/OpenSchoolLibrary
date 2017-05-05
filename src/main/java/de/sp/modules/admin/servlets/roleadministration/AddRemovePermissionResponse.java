package de.sp.modules.admin.servlets.roleadministration;

/**
 * Created by Martin on 03.05.2017.
 */
public class AddRemovePermissionResponse {

    private String status;
    private String message;

    public AddRemovePermissionResponse(String status, String message) {
        this.status = status;
        this.message = message;
    }
}

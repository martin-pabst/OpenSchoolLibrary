package de.sp.modules.admin.servlets.roleadministration;

import de.sp.modules.admin.servlets.useradministration.RoleData;

/**
 * Created by martin on 04.05.2017.
 */
public class SaveRoleResponse {

    public String status; // "error" or "success"
    public String message; // if status == "error"

    public RoleData record;

    public SaveRoleResponse(String status, String message, RoleData record) {
        this.status = status;
        this.message = message;
        this.record = record;
    }
}

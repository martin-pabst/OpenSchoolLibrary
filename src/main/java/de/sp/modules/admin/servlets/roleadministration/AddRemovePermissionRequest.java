package de.sp.modules.admin.servlets.roleadministration;

/**
 * Created by Martin on 03.05.2017.
 */
public class AddRemovePermissionRequest {

/*
    school_id: global_school_id, role_id: role.id,
    permission_name: recid, addRemove: isSelected ? 'remove' : 'add'
*/


    public Long school_id;
    public Long role_id;
    public String permission_name;
    public String addRemove;


}

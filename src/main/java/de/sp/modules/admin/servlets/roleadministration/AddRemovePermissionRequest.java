package de.sp.modules.admin.servlets.roleadministration;

import de.sp.tools.validation.BaseRequestData;
import de.sp.tools.validation.Validation;

/**
 * Created by Martin on 03.05.2017.
 */
public class AddRemovePermissionRequest extends BaseRequestData{

/*
    school_id: global_school_id, role_id: role.id,
    permission_name: recid, addRemove: isSelected ? 'remove' : 'add'
*/

    @Validation(notNull = true)
    public Long school_id;

    @Validation(notNull = true)
    public Long role_id;

    public String permission_name;

    @Validation(acceptedValues = {"add", "remove"})
    public String addRemove;


}

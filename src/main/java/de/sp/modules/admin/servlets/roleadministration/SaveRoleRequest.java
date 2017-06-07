package de.sp.modules.admin.servlets.roleadministration;

import de.sp.modules.admin.servlets.useradministration.RoleData;
import de.sp.tools.validation.BaseRequestData;
import de.sp.tools.validation.Validation;

/**
 * Created by martin on 04.05.2017.
 */
public class SaveRoleRequest extends BaseRequestData{

    public String cmd; // not used on server side

    @Validation(notNull = true)
    public Long recid;

    @Validation(notNull = true)
    public Long school_id;

    @Validation
    public RoleData record;


}

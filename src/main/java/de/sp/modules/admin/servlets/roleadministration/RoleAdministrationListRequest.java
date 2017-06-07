package de.sp.modules.admin.servlets.roleadministration;

import de.sp.tools.validation.BaseRequestData;
import de.sp.tools.validation.Validation;

/**
 * Created by martin on 03.05.2017.
 */
public class RoleAdministrationListRequest extends BaseRequestData{

    @Validation(notNull = true)
    public Long school_id;

}

package de.sp.modules.admin.servlets.useradministration;

import de.sp.tools.validation.BaseRequestData;
import de.sp.tools.validation.Validation;

/**
 * Created by Martin on 03.05.2017.
 */
public class AddRemoveRoleRequest extends BaseRequestData{

    @Validation(notNull = true)
    public Long school_id;

    @Validation(notNull = true)
    public Long user_id;

    @Validation(notNull = true)
    public Long role_id;

    @Validation(acceptedValues = {"add", "remove"})
    public String addRemove;

}

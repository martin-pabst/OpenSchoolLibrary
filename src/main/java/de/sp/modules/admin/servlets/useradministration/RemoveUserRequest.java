package de.sp.modules.admin.servlets.useradministration;

import de.sp.tools.validation.BaseRequestData;
import de.sp.tools.validation.Validation;

/**
 * Created by martin on 04.05.2017.
 */
public class RemoveUserRequest extends BaseRequestData{

    @Validation(notNull = true)
    public Long school_id;

    @Validation(min = 1)
    public Long[] user_ids;

}

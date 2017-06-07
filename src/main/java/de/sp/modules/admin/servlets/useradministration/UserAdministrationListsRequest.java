package de.sp.modules.admin.servlets.useradministration;

import de.sp.tools.validation.BaseRequestData;
import de.sp.tools.validation.Validation;

/**
 * Created by martin on 03.05.2017.
 */
public class UserAdministrationListsRequest extends BaseRequestData{

    @Validation(notNull = true)
    public Long school_id;

}

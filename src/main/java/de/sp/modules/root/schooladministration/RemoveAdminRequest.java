package de.sp.modules.root.schooladministration;

import de.sp.tools.validation.BaseRequestData;
import de.sp.tools.validation.Validation;

/**
 * Created by martin on 04.05.2017.
 */
public class RemoveAdminRequest extends BaseRequestData{

    @Validation(notNull = true)
    public Long admin_id;

}

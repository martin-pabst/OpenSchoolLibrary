package de.sp.modules.root.schooladministration;

import de.sp.tools.validation.BaseRequestData;
import de.sp.tools.validation.Validation;

/**
 * Created by martin on 04.05.2017.
 */
public class RemoveSchoolRequest extends BaseRequestData{

    @Validation(notNull = true)
    public Long school_id;

}

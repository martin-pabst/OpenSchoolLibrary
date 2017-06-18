package de.sp.modules.root.schooladministration;

import de.sp.tools.validation.BaseRequestData;
import de.sp.tools.validation.Validation;

/**
 * Created by martin on 04.05.2017.
 */
public class SaveAdminRequest extends BaseRequestData{

    public String cmd; // not used server side

    @Validation(notNull = true)
    public Long recid;

    @Validation(notNull = true)
    public Long school_id;

    @Validation
    public AdminData record;


}

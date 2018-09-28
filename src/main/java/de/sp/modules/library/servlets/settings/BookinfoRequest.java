package de.sp.modules.library.servlets.settings;

import de.sp.tools.validation.BaseRequestData;
import de.sp.tools.validation.Validation;

/**
 * Created by Martin on 29.04.2017.
 */
public class BookinfoRequest extends BaseRequestData{

    @Validation(notNull = true)
    public Long school_id;

    @Validation(notEmpty = true)
    public String barcode;

}

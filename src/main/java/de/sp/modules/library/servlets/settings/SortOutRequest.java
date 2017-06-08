package de.sp.modules.library.servlets.settings;

import de.sp.tools.validation.BaseRequestData;
import de.sp.tools.validation.Validation;

import java.util.Date;

/**
 * Created by Martin on 06.05.2017.
 */
public class SortOutRequest extends BaseRequestData{

    @Validation(notEmpty = true)
    public String barcode;

    @Validation(notNull = true)
    public Long school_id;

    public Date sort_out_date;

}

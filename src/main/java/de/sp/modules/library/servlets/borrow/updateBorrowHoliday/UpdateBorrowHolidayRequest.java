package de.sp.modules.library.servlets.borrow.updateBorrowHoliday;

import de.sp.tools.validation.BaseRequestData;
import de.sp.tools.validation.Validation;

/**
 * Created by Martin on 23.05.2017.
 */
public class UpdateBorrowHolidayRequest extends BaseRequestData{

    public String cmd;

    @Validation(notNull = true)
    public Long school_id;

    @Validation(notNull = true)
    public Long id;

    public String field;
    public Boolean value_new;


}

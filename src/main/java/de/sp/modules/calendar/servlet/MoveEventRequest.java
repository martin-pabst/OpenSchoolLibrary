package de.sp.modules.calendar.servlet;

import de.sp.tools.validation.BaseRequestData;
import de.sp.tools.validation.Validation;

import java.util.Date;

/**
 * Created by Martin on 21.05.2017.
 */
public class MoveEventRequest extends BaseRequestData {

    @Validation(notNull = true)
    public Long id;

    @Validation(notNull = true)
    public Long school_id;

    public Boolean allDay;

    @Validation(notNull = true)
    public Date start;
    public Date end;

}

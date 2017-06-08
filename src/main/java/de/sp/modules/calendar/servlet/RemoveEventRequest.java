package de.sp.modules.calendar.servlet;

import de.sp.tools.validation.BaseRequestData;
import de.sp.tools.validation.Validation;

/**
 * Created by Martin on 28.05.2017.
 */
public class RemoveEventRequest extends BaseRequestData{

    @Validation(notNull = true)
    public Long school_id;

    @Validation(notNull = true)
    public Long event_id;

}

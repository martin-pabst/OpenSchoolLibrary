package de.sp.modules.calendar.servlet;

import de.sp.tools.validation.BaseRequestData;
import de.sp.tools.validation.Validation;

/**
 * Created by Martin on 21.05.2017.
 */
public class GetEventDetailsRequest extends BaseRequestData {

    @Validation(notNull = true)
    public Long school_id;

    @Validation(notNull = true)
    public Long school_term_id;

    @Validation(notNull = true)
    public Long event_id;

}

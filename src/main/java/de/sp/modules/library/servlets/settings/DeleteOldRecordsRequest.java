package de.sp.modules.library.servlets.settings;

import de.sp.tools.validation.BaseRequestData;
import de.sp.tools.validation.Validation;

import java.util.Date;

/**
 * Created by Martin on 29.04.2017.
 */
public class DeleteOldRecordsRequest extends BaseRequestData{

    @Validation(notNull = true)
    public Long school_id;

    @Validation(notNull = true)
    public Date date_from;

}

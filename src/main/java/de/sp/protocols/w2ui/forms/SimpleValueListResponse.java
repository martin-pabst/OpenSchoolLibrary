package de.sp.protocols.w2ui.forms;

import de.sp.tools.validation.Validation;

/**
 * Created by Martin on 22.04.2017.
 */
public class SimpleValueListResponse {
    // {"id":1,"text":"8a","hidden":false}
    @Validation(notNull = true)
    public Long id;
    public String text;
    public boolean hidden;

}

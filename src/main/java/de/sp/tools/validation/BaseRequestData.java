package de.sp.tools.validation;

import de.sp.main.services.text.TS;

/**
 * Created by Martin on 07.06.2017.
 */
public class BaseRequestData {

    public void validate(TS ts) throws ValidationException {
        Validator.validate(this, ts);
    }


}

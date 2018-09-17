package de.sp.modules.user.servlets;

import de.sp.tools.validation.BaseRequestData;
import de.sp.tools.validation.Validation;

public class ChangePasswordRequest extends BaseRequestData {

    @Validation(notNull = true)
    private long school_id;

    @Validation(notEmpty = true)
    private String oldPassword;

    @Validation(notEmpty = true)
    private String newPassword1;

    @Validation(notEmpty = true)
    private String newPassword2;

    public long getSchool_id() {
        return school_id;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public String getNewPassword1() {
        return newPassword1;
    }

    public String getNewPassword2() {
        return newPassword2;
    }
}

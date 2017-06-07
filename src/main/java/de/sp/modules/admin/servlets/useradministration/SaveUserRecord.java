package de.sp.modules.admin.servlets.useradministration;

import de.sp.tools.validation.Validation;

/**
 * Created by martin on 04.05.2017.
 */
public class SaveUserRecord {

    @Validation(maxLength = 30)
    public String username;

    @Validation(maxLength = 200)
    public String name;

    @Validation(min = 0, max = 1)
    public int is_admin;

    @Validation(maxLength = 1000)
    public String password;

    public Long id; // null if new user
}

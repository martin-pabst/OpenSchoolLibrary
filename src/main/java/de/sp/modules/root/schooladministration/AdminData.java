package de.sp.modules.root.schooladministration;

import de.sp.database.model.User;
import de.sp.tools.validation.Validation;

/**
 * Created by martin on 04.05.2017.
 */
public class AdminData {

    @Validation(maxLength = 30)
    public String username;

    @Validation(maxLength = 200)
    public String name;

    @Validation(maxLength = 1000)
    public String password;

    public Long id; // null if new user


    public AdminData(Long id, String username, String name, String password) {
        this.username = username;
        this.name = name;
        this.password = password;
        this.id = id;
    }

    public AdminData(User user) {

        name = user.getName();
        username = user.getUsername();
        id = user.getId();

    }
}

package de.sp.modules.root.schooladministration;

import de.sp.database.model.Role;
import de.sp.database.model.School;
import de.sp.database.model.User;
import de.sp.database.stores.UserRolePermissionStore;
import de.sp.tools.validation.Validation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Martin on 16.06.2017.
 */
public class SchoolData {

    // == null, if new school schould get created
    public Long id;

    @Validation(notEmpty = true, maxLength = 10)
    public String number;

    @Validation(notEmpty = true, maxLength = 200)
    public String name;

    @Validation(maxLength = 20)
    public String abbreviation;

    @Validation
    public List<AdminData> admins = new ArrayList<>();

    public SchoolData(){};

    public SchoolData(School school) {

        id = school.getId();
        number = school.getNumber();
        name = school.getName();
        abbreviation = school.getAbbreviation();

        Long adminRoleId = null;

        for (Role role : UserRolePermissionStore.getInstance().getRoleListBySchoolId(school.getId())) {
            if("admin".equals(role.getName())){
                adminRoleId = role.getId();
                break;
            }
        }

        for (User user : UserRolePermissionStore.getInstance().getUserListBySchoolId(school.getId())) {
            if(user.hasRole(adminRoleId)){
                admins.add(new AdminData(user));
            }
        }

    }
}

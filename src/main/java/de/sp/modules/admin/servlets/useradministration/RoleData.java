package de.sp.modules.admin.servlets.useradministration;

import de.sp.main.services.text.TS;
import de.sp.tools.validation.Validation;

import java.util.regex.Pattern;

/**
 * Created by martin on 03.05.2017.
 */
public class RoleData {

    @Validation
    public Long id;

    @Validation(notEmpty = true, maxLength = 40)
    public String name;

    @Validation(maxLength = 40)
    public String translated_name;

    @Validation(maxLength = 500)
    public String remark;

    @Validation(notNull = true)
    public String permissions = "";

    public String[] permissionIdentifierList = new String[]{};

    public void init(TS ts){

        permissionIdentifierList = permissions.split(Pattern.quote("|"));

        translated_name = ts.get("roles." + name);

        if(translated_name.startsWith("roles.")){
            translated_name = name;
        }


    }


}

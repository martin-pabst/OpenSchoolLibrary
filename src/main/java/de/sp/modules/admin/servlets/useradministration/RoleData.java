package de.sp.modules.admin.servlets.useradministration;

import de.sp.main.resources.text.TS;

import java.util.regex.Pattern;

/**
 * Created by martin on 03.05.2017.
 */
public class RoleData {

    public Long id;

    public String name;

    public String translated_name;

    public String remark;

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

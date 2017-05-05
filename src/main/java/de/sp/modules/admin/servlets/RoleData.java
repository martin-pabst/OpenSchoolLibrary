package de.sp.modules.admin.servlets;

import de.sp.main.resources.text.TS;

import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by martin on 03.05.2017.
 */
public class RoleData {

    public long id;

    public String name;

    public String translated_name;

    public String remark;

    public String permissions;

    public String[] permissionIdentifierList;

    public void init(TS ts){

        permissionIdentifierList = permissions.split(Pattern.quote("|"));

        translated_name = ts.get("roles." + name);

        if(translated_name.startsWith("roles.")){
            translated_name = name;
        }


    }


}

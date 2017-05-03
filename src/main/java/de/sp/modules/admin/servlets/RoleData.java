package de.sp.modules.admin.servlets;

import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by martin on 03.05.2017.
 */
public class RoleData {

    public long id;

    public String name;

    public String remark;

    public String permissions;

    public String[] permissionIdentifierList;

    public void init(){

        permissionIdentifierList = permissions.split(Pattern.quote("|"));

    }


}

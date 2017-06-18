package de.sp.modules.admin.servlets.useradministration;

import java.util.List;

/**
 * Created by martin on 03.05.2017.
 *
 * This class is only used in response-objects, so no validation needed.
 */
public class UserData {

    public long id;
    public String username;
    public String name;
    public String languageCode;
    public Boolean is_root = false;

    public Long role_id; // Dummy for flat SQL-Statement

    public List<Long> role_ids;
    

}

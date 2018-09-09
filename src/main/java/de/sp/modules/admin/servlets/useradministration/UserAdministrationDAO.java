package de.sp.modules.admin.servlets.useradministration;

import de.sp.database.statements.StatementStore;
import de.sp.main.services.text.TS;
import org.sql2o.Connection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by martin on 03.05.2017.
 */
public class UserAdministrationDAO {

    public static List<UserData> getUserList(Connection con, Long school_id){

        String sql = StatementStore.getStatement("admin.userAdministration.getUserList");

        List<UserData> userdataFlat = con.createQuery(sql)
                .addParameter("school_id", school_id)
                .executeAndFetch(UserData.class);

        List<UserData> userDataList = new ArrayList<>();

        HashMap<Long, UserData> userIdToUserMap = new HashMap<>();

        for (UserData userData : userdataFlat) {
            
            UserData ud = userIdToUserMap.get(userData.id);
            
            if(ud == null){
                userData.role_ids = new ArrayList<>();
                if(userData.role_id != null) {
                    userData.role_ids.add(userData.role_id);
                }
                userDataList.add(userData);
                userIdToUserMap.put(userData.id, userData);
            } else {
                ud.role_ids.add(userData.role_id);
            }
            
        }
        
        return userDataList;

    }

    public static List<RoleData> getRolesList(Connection con, Long school_id, TS ts){

        String sql = StatementStore.getStatement("admin.userAdministration.getRoleList");

        List<RoleData> roleList = con.createQuery(sql)
                .addParameter("school_id", school_id)
                .executeAndFetch(RoleData.class);

        for (RoleData roleData : roleList) {
            roleData.init(ts);
        }

        return roleList;

    }


}

package de.sp.modules.admin.daos;

import de.sp.database.statements.StatementStore;
import de.sp.modules.admin.servlets.RoleData;
import de.sp.modules.admin.servlets.UserData;
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
                userData.role_ids.add(userData.role_id);
                userDataList.add(userData);
                userIdToUserMap.put(userData.id, userData);
            } else {
                ud.role_ids.add(userData.role_id);
            }
            
        }
        
        return userDataList;

    }

    public static List<RoleData> getRolesList(Connection con, Long school_id){

        String sql = StatementStore.getStatement("admin.userAdministration.getRoleList");

        List<RoleData> roleList = con.createQuery(sql)
                .addParameter("school_id", school_id)
                .executeAndFetch(RoleData.class);

        for (RoleData roleData : roleList) {
            roleData.init();
        }

        return roleList;

    }


}

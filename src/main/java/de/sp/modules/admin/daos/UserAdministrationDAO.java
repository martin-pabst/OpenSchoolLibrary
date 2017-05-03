package de.sp.modules.admin.daos;

import de.sp.database.statements.StatementStore;
import de.sp.modules.admin.servlets.UserData;
import org.sql2o.Connection;

import java.util.List;

/**
 * Created by martin on 03.05.2017.
 */
public class UserAdministrationDAO {

    public static List<UserData> getUserList(Connection con, Long school_id){

        String sql = StatementStore.getStatement("admin.userAdministration.getUserList");

        return con.createQuery(sql)
                .addParameter("school_id", school_id)
                .executeAndFetch(UserData.class);


    }


}

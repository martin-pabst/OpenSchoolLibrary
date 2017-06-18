package de.sp.modules.root.schooladministration;

import de.sp.database.statements.StatementStore;
import org.sql2o.Connection;

import java.util.List;

/**
 * Created by Martin on 17.06.2017.
 */
public class SchoolAdministrationDAO {

    public static List<SchoolData> getSchoolData(Connection con){

        String sql = StatementStore.getStatement("schoolAdministration.getSchoolList");

        return con.createQuery(sql).executeAndFetch(SchoolData.class);

    }




}

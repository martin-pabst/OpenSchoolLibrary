package de.sp.database.daos.basic;

import de.sp.database.model.Absence;
import de.sp.database.model.Calendar;
import de.sp.database.statements.StatementStore;
import org.sql2o.Connection;

import java.util.List;

/**
 * Created by martin on 10.05.2017.
 */
public class AbsenceDAO {

    public static List<Absence> getAll(Connection con){

        String sql = StatementStore.getStatement("absence.getAll");

        return con.createQuery(sql)
                .executeAndFetch(Absence.class);

    }
    


}

package de.sp.database.daos.basic;

import de.sp.database.model.Calendar;
import de.sp.database.model.CalendarRestriction;
import de.sp.database.statements.StatementStore;
import org.sql2o.Connection;

import java.util.List;

/**
 * Created by martin on 10.05.2017.
 */
public class CalendarRestrictionDAO {

    public static List<CalendarRestriction> getAll (Connection con){

        String sql = StatementStore.getStatement("calendar_restriction.getAll");

        return con.createQuery(sql)
                .executeAndFetch(CalendarRestriction.class);

    }
    


}
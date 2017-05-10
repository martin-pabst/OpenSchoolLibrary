package de.sp.database.daos.basic;

import de.sp.database.model.Calendar;
import de.sp.database.model.User;
import de.sp.database.statements.StatementStore;
import de.sp.main.resources.modules.InsufficientPermissionException;
import de.sp.modules.calendar.CalendarModule;
import org.sql2o.Connection;

import java.util.Date;
import java.util.List;

/**
 * Created by martin on 10.05.2017.
 */
public class CalendarDAO {

    public static List<Calendar> getAllWithoutDescriptions(Connection con){

        String sql = StatementStore.getStatement("calendar.getAllWithoutDescription");

        return con.createQuery(sql)
                .executeAndFetch(Calendar.class);

    }

    public static void complementDescription(Calendar calendar, Connection con)
    throws InsufficientPermissionException {

        String sql = StatementStore.getStatement("calendar.getDescription");

        String description = con.createQuery(sql)
                .addParameter("calendar_id", calendar.getId())
                .executeAndFetchFirst(String.class);

        calendar.setDescription(description);

    }

}

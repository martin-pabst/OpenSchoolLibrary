package de.sp.database.daos.basic;

import de.sp.database.model.Event;
import de.sp.database.statements.StatementStore;
import org.sql2o.Connection;

import java.util.List;

/**
 * Created by martin on 10.05.2017.
 */
public class EventDAO {

    public static List<Event> getAllWithoutDescriptions(Connection con){

        String sql = StatementStore.getStatement("event.getAllWithoutDescription");

        return con.createQuery(sql)
                .addColumnMapping("start_timestamp", "start")
                .addColumnMapping("end_timestamp", "end")
                .executeAndFetch(Event.class);

    }

    public static void addDescription(Event event, Connection con){

        String sql = StatementStore.getStatement("event.getDescription");

        String description = con.createQuery(sql)
                .addParameter("calendar_id", event.getId())
                .executeAndFetchFirst(String.class);

        event.setDescription(description);

    }

    public static void insert (Event event, Connection con){

        String sql = StatementStore.getStatement("event.insert");

        Long id = con
                .createQuery(sql, true)
                .bind(event)
                .executeUpdate()
                .getKey(Long.class);

        event.setId(id);

    }

}

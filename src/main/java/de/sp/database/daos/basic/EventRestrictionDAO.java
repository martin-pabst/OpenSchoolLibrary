package de.sp.database.daos.basic;

import de.sp.database.model.EventRestriction;
import de.sp.database.statements.StatementStore;
import org.sql2o.Connection;

import java.util.List;

/**
 * Created by martin on 10.05.2017.
 */
public class EventRestrictionDAO {

    public static List<EventRestriction> getAll (Connection con){

        String sql = StatementStore.getStatement("event_restriction.getAll");

        return con.createQuery(sql)
                .executeAndFetch(EventRestriction.class);

    }

    public static void insert (EventRestriction eventRestriction, Connection con){

        String sql = StatementStore.getStatement("event_restriction.insert");

        Long id = con
                .createQuery(sql, true)
                .bind(eventRestriction)
                .executeUpdate()
                .getKey(Long.class);

        eventRestriction.setId(id);

    }


    public static void remove(EventRestriction eventRestriction, Connection con) {

        String sql = StatementStore.getStatement("event_restriction.remove");

        con.createQuery(sql)
                .addParameter("id", eventRestriction.getId())
                .executeUpdate();

    }
}

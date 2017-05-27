package de.sp.database.daos.basic;

import de.sp.database.model.Absence;
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

    public static void insert (Absence absence, Connection con){

        String sql = StatementStore.getStatement("absence.insert");

        Long id = con
                .createQuery(sql, true)
                .bind(absence)
                .executeUpdate()
                .getKey(Long.class);

        absence.setId(id);

    }


    public static void remove(Absence absence, Connection con) {

        String sql = StatementStore.getStatement("absence.remove");

        con.createQuery(sql)
                .addParameter("id", absence.getId())
                .executeUpdate();

    }
}

package de.sp.database.daos.basic;

import de.sp.database.model.DBClass;
import de.sp.database.statements.StatementStore;
import de.sp.main.mainframe.definitionsservlet.SimpleValueListEntry;
import org.sql2o.Connection;

import java.util.ArrayList;
import java.util.List;

public class DBClassDAO {
    public static List<DBClass> getAll(Connection con) {

        String sql = StatementStore.getStatement("class.getAll");
        List<DBClass> classlist = con.createQuery(sql).executeAndFetch(
                DBClass.class);
        return classlist;

    }

    public static DBClass insert(Long school_term_id, String name,
                                 Integer year_of_school, Long form_id, Connection con)
            throws Exception {

        String sql = StatementStore.getStatement("class.insert");

        Long id = con.createQuery(sql, true)
                .addParameter("school_term_id", school_term_id)
                .addParameter("name", name)
                .addParameter("year_of_school", year_of_school)
                .addParameter("form_id", form_id).executeUpdate()
                .getKey(Long.class);

        return new DBClass(id, school_term_id, name, year_of_school, form_id);

    }

    public static void delete(DBClass dbClass, Connection con) {

        String sql = StatementStore.getStatement("class.delete");

        con.createQuery(sql)

                .addParameter("id", dbClass.getId())

                .executeUpdate();

    }

    public static List<DBClass> findBySchoolTermId(long school_term_id, Connection con) {

        String sql = StatementStore.getStatement("class.findBySchoolTermId");

        List<DBClass> classlist = con.createQuery(sql)
                .addParameter("school_term_id", school_term_id)
                .executeAndFetch(DBClass.class);

        return classlist;

    }

    public static List<SimpleValueListEntry> getSimpleValueList(long school_term_id, Connection con){
        List<DBClass> classList = findBySchoolTermId(school_term_id, con);
        ArrayList<SimpleValueListEntry> simpleValueList = new ArrayList<>();

        classList.forEach(dbClass -> {simpleValueList.add(new SimpleValueListEntry(dbClass.getId(), dbClass.getName()));});

        return simpleValueList;
    }
}

package de.sp.database.daos.basic;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.sql2o.Connection;

import de.sp.database.model.Term;
import de.sp.database.statements.StatementStore;

public class TermDAO {

	public static List<Term> getAll(Connection con) {

		String sql = StatementStore.getStatement("term.getAll");

		List<Term> termlist = con.createQuery(sql).executeAndFetch(Term.class);

		return termlist;

	}

	public static Map<Long, Term> termListToKeyMap(List<Term> terms) {

		Map<Long, Term> keyMap = new HashMap<>();

		for (Term term : terms) {
			keyMap.put(term.getId(), term);
		}

		return keyMap;

	}

	public static Term insert(String name, Date begindate, Date enddate,
			Connection con) throws Exception {

		String sql = StatementStore.getStatement("term.insert");

		Long id = con.createQuery(sql, true).addParameter("name", name)
				.addParameter("begindate", begindate)
				.addParameter("enddate", enddate).executeUpdate()
				.getKey(Long.class);

		return new Term(id, name, begindate, enddate);

	}

	public static void delete(Term term, Connection con) {

		String sql = StatementStore.getStatement("term.delete");

		con.createQuery(sql)

		.addParameter("id", term.getId())

		.executeUpdate();

	}
}

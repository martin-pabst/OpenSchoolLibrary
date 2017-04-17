package de.sp.asvsst.database.daos;

import org.sql2o.Connection;

import de.sp.database.statements.StatementStore;

public class ASVSStDAO {

	public static void deleteStudentClassReferendes(Long school_term_id,
			Connection con) {

		String sql = StatementStore
				.getStatement("asvsst.deleteStudentClassReferences");

		con.createQuery(sql).addParameter("school_term_id", school_term_id)
				.executeUpdate();

	}

}

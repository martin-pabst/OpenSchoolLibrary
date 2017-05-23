package de.sp.database.daos.basic;

import de.sp.database.model.Borrows;
import de.sp.database.statements.StatementStore;
import org.sql2o.Connection;

import java.util.Date;
import java.util.List;

public class BorrowsDAO {
	public static List<Borrows> getAll(Connection con) {

		String sql = StatementStore.getStatement("borrows.getAll");
		List<Borrows> borrowslist = con.createQuery(sql).executeAndFetch(
				Borrows.class);
		return borrowslist;

	}

	public static Borrows insert(Long book_copy_id, Long student_id,
			Long teacher_id, Date begindate, Date enddate, Date return_date,
			String remarks, Boolean over_holidays, Connection con) throws Exception {

		String sql = StatementStore.getStatement("borrows.insert");

		Long id = con.createQuery(sql, true)
				.addParameter("book_copy_id", book_copy_id)
				.addParameter("student_id", student_id)
				.addParameter("teacher_id", teacher_id)
				.addParameter("begindate", begindate)
				.addParameter("enddate", enddate)
				.addParameter("return_date", return_date)
				.addParameter("remarks", remarks)
				.addParameter("over_holidays", over_holidays)
				.executeUpdate()
				.getKey(Long.class);

		return new Borrows(id, book_copy_id, student_id, teacher_id, begindate,
				enddate, return_date, remarks, over_holidays);

	}

	public static void delete(Long borrows_id, Connection con) {

		String sql = StatementStore.getStatement("borrows.delete");

		con.createQuery(sql)

		.addParameter("id", borrows_id)

		.executeUpdate();

	}

	public static Long findSchoolId(Long borrows_id, Connection con) {

		String sql = StatementStore.getStatement("borrows.findSchoolId");

		return con.createQuery(sql).addParameter("borrows_id", borrows_id)
				.executeAndFetchFirst(Long.class);

	}

	public static void setReturnDate(Long borrows_id, Date return_date,
			Connection con) {

		String sql = StatementStore.getStatement("borrows.setReturnDate");

		con.createQuery(sql)

		.addParameter("borrows_id", borrows_id)
				.addParameter("return_date", return_date)

				.executeUpdate();

	}

	public static void setOverHolidays(Long borrows_id, Boolean over_holidays,
			Connection con) {

		String sql = StatementStore.getStatement("borrows.setOverHolidays");

		con.createQuery(sql)

		.addParameter("borrows_id", borrows_id)
				.addParameter("over_holidays", over_holidays)

				.executeUpdate();

	}


}

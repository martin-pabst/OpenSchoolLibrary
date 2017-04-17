package de.sp.database.daos.basic;

import java.util.Date;
import java.util.List;

import org.sql2o.Connection;

import de.sp.database.model.Fee;
import de.sp.database.statements.StatementStore;
import de.sp.modules.library.servlets.returnbooks.scanbarcodeservlet.FeeRecord;

public class FeeDAO {
	public static List<Fee> getAll(Connection con) {

		String sql = StatementStore.getStatement("fee.getAll");
		List<Fee> feelist = con.createQuery(sql).executeAndFetch(Fee.class);
		return feelist;

	}

	public static Fee insert(Long borrows_id, double amount, String remarks,
			Date paid_date, Connection con) throws Exception {

		String sql = StatementStore.getStatement("fee.insert");

		Long id = con.createQuery(sql, true)
				.addParameter("borrows_id", borrows_id)
				.addParameter("amount", amount)
				.addParameter("remarks", remarks)
				.addParameter("paid_date", paid_date).executeUpdate()
				.getKey(Long.class);

		return new Fee(id, borrows_id, amount, remarks, paid_date);

	}

	public static void delete(Long fee_id, Connection con) {

		String sql = StatementStore.getStatement("fee.delete");

		con.createQuery(sql)

		.addParameter("id", fee_id)

		.executeUpdate();

	}

	public static List<FeeRecord> findByBorrowsId(Long borrows_id,
			Connection con) {
		String sql = StatementStore.getStatement("fee.findByBorrowsId");
		List<FeeRecord> feelist = con.createQuery(sql)
				.addParameter("borrows_id", borrows_id)
				.executeAndFetch(FeeRecord.class);
		return feelist;
	}

	public static List<FeeRecord> findByTeacherId(Long teacher_id,
			boolean onlyUnpaidFees, Connection con) {
		String sql = StatementStore.getStatement("fee.findByTeacherId");

		if (onlyUnpaidFees) {
			sql += " and fee.paid_date is null";
		}

		List<FeeRecord> feelist = con.createQuery(sql)
				.addParameter("teacher_id", teacher_id)
				.executeAndFetch(FeeRecord.class);
		return feelist;
	}

	public static List<FeeRecord> findByStudentId(Long student_id,
			boolean onlyUnpaidFees, Connection con) {
		String sql = StatementStore.getStatement("fee.findByStudentId");

		if (onlyUnpaidFees) {
			sql += " and fee.paid_date is null";
		}

		List<FeeRecord> feelist = con.createQuery(sql)
				.addParameter("student_id", student_id)
				.executeAndFetch(FeeRecord.class);
		return feelist;
	}

	public static Long getSchoolForFee(Long fee_id, Connection con) {
		String sql = StatementStore.getStatement("fee.getSchoolForFee");
		Long school_id = con.createQuery(sql).addParameter("fee_id", fee_id)
				.executeAndFetchFirst(Long.class);
		return school_id;

	}

	public static void update(Fee fee, Connection con) {

		String sql = StatementStore.getStatement("fee.update");

		con.createQuery(sql).addParameter("id", fee.getId())
				.addParameter("borrows_id", fee.getBorrows_id())
				.addParameter("amount", fee.getAmount())
				.addParameter("remarks", fee.getRemarks())
				.addParameter("paid_date", fee.getPaid_date()).executeUpdate();

	}

	public static void paymentsDone(Long student_id, Long teacher_id,
			Date paid_date, Connection con) {

		String sql = StatementStore.getStatement("fee.paymentsDone");

		con.createQuery(sql).addParameter("student_id", student_id)
				.addParameter("teacher_id", teacher_id)
				.addParameter("paid_date", paid_date).executeUpdate();

	}

}

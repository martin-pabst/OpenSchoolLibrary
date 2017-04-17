package de.sp.modules.library.servlets.returnbooks.scanbarcodeservlet;

import java.util.Date;

public class FeeUpdateSaveRequest {

	private Long school_id;

	private Long borrows_id;

	private double amount;

	private String remarks;

	private Date paid_date;

	private Long fee_id;

	public Long getFee_id() {
		return fee_id;
	}

	public Long getSchool_id() {
		return school_id;
	}

	public Long getBorrows_id() {
		return borrows_id;
	}

	public double getAmount() {
		return amount;
	}

	public String getRemarks() {
		return remarks;
	}

	public Date getPaid_date() {
		return paid_date;
	}

}

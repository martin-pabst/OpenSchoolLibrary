package de.sp.modules.library.servlets.returnbooks.scanbarcodeservlet;

import de.sp.tools.validation.BaseRequestData;
import de.sp.tools.validation.Validation;

import java.util.Date;

public class FeeUpdateSaveRequest extends BaseRequestData{

	@Validation(notNull = true)
	private Long school_id;

	@Validation(notNull = true)
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

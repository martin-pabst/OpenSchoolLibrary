package de.sp.modules.library.servlets.returnbooks.scanbarcodeservlet;

import java.util.Date;

@SuppressWarnings("unused")
public class FeeRecord {

	private Long fee_id;

	private Long borrows_id;

	private double amount;

	private String remarks;

	private Date paid_date;

	private long book_id;
	private String title;
	private String author;

	private long book_copy_id;
	private String barcode;

}

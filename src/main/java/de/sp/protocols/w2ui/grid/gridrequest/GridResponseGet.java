package de.sp.protocols.w2ui.grid.gridrequest;

import java.util.ArrayList;
import java.util.List;

public class GridResponseGet<E> {
	
//	"status"	: "success",
//	"total"		: 36,
//	"records"	: [
//		{ "recid": 1, "field-1": "value-1", ... "field-N": "value-N" },
//		...
//		{ "recid": N, "field-1": "value-1", ... "field-N": "value-N" }
//	]
	

	private GridResponseStatus status;
	
	private int total;
	
	private List<E> records = new ArrayList<>();
	
	private String message = "";
	
	@SuppressWarnings("unused")
	private Long id;

	public GridResponseGet(GridResponseStatus status, int total,
			List<E> records, String message, Long id) {
		super();
		this.status = status;
		this.total = total;
		this.records = records;
		this.id = id;
		this.message = message;
	}

	public GridResponseGet(GridResponseStatus status, String message) {
		this(status, 0, null, message, null);
	}

	public GridResponseStatus getStatus() {
		return status;
	}

	public int getTotal() {
		return total;
	}

	public List<E> getRecords() {
		return records;
	}

	public String getMessage() {
		return message;
	}
	

	
}

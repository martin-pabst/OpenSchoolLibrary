package de.sp.protocols.w2ui.grid.gridrequest;


public class GridResponseUpdateDelete {

	// "status" : "success",
	// "message" : "Error..."

	private GridResponseStatus status;

	private String message = "";

	public GridResponseUpdateDelete(GridResponseStatus status, String message) {
		super();
		this.status = status;
		this.message = message;
	}

	public GridResponseStatus getStatus() {
		return status;
	}

	public String getMessage() {
		return message;
	}

}

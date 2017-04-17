package de.sp.protocols.w2ui.grid.gridrequest;


@SuppressWarnings("unused")
public class GridResponseSave {
	
//	"status"	: "success",
//	"message"	: "Error...",
//	"id"		: 18

	private GridResponseStatus status;
	
	private String message = "";
	
	private Long id;
	
	private Object info;
	

	public GridResponseSave(GridResponseStatus status, String message, Long id) {
		super();
		this.status = status;
		this.message = message;
		this.id = id;
	}

	public GridResponseStatus getStatus() {
		return status;
	}

	public String getMessage() {
		return message;
	}

	public Long getId() {
		return id;
	}

	public void setInfo(Object info) {
		this.info = info;
	}
	

	
}

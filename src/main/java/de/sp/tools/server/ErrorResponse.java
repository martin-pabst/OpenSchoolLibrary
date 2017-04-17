package de.sp.tools.server;

@SuppressWarnings("unused")
public class ErrorResponse {
	
	private String status = "error";
	
	private String message;

	public ErrorResponse(String message) {
		super();
		this.message = message;
	}
	
	
	
}

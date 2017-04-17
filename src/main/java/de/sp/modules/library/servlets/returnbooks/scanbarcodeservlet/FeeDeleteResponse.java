package de.sp.modules.library.servlets.returnbooks.scanbarcodeservlet;

import de.sp.protocols.w2ui.grid.gridrequest.GridResponseStatus;

@SuppressWarnings("unused")
public class FeeDeleteResponse {

	private GridResponseStatus status;

	private String message;


	public FeeDeleteResponse(GridResponseStatus status, String message) {
		super();
		this.status = status;
		this.message = message;
	}


	

}

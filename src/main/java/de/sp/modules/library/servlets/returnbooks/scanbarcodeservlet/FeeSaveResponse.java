package de.sp.modules.library.servlets.returnbooks.scanbarcodeservlet;

import de.sp.protocols.w2ui.grid.gridrequest.GridResponseStatus;

@SuppressWarnings("unused")
public class FeeSaveResponse {

	private GridResponseStatus status;

	private String message;

	private Long fee_id;

	public FeeSaveResponse(GridResponseStatus status, String message,
			Long fee_id) {
		super();
		this.status = status;
		this.message = message;
		this.fee_id = fee_id;
	}


	

}

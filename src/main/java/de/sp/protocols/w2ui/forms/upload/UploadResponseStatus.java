package de.sp.protocols.w2ui.forms.upload;

import de.sp.protocols.w2ui.forms.ResponseStatusEnum;

public class UploadResponseStatus {
	
	private String status;
	private String message;
	private String progressCode;
	
	public UploadResponseStatus(ResponseStatusEnum status, String message, String progressCode) {
		super();
		this.status = status.getText();
		this.message = message;
		this.progressCode = progressCode;
	}

	public String getStatus() {
		return status;
	}

	public String getMessage() {
		return message;
	}

	public String getProgressCode() {
		return progressCode;
	}
	
	
}

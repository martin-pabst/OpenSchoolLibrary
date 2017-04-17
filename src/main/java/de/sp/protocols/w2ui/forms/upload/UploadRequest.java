package de.sp.protocols.w2ui.forms.upload;

public class UploadRequest {

	private String cmd;
	private int recid;

	private Long school_id;

	private UploadRequestRecord record;

	public String getCmd() {
		return cmd;
	}

	public int getRecid() {
		return recid;
	}

	public UploadRequestRecord getRecord() {
		return record;
	}

	public boolean isSaveRecordCommand() {

		return "save-record".equals(cmd) && record != null;

	}

	public Long getSchool_id() {
		return school_id;
	}

}

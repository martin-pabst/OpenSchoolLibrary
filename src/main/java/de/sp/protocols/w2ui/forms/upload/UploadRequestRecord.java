package de.sp.protocols.w2ui.forms.upload;

import java.util.List;

public class UploadRequestRecord {
	
	List<UploadRequestFile> file;
	String zippassword;

	public List<UploadRequestFile> getFile() {
		return file;
	}

	public boolean hasFiles() {
		return file != null && file.size() > 0;
	}

	public String getZippassword() {
		return zippassword;
	}
	
	
	
}

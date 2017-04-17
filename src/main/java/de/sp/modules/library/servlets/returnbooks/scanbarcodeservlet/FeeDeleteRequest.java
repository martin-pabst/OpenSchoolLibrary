package de.sp.modules.library.servlets.returnbooks.scanbarcodeservlet;

public class FeeDeleteRequest {
	
	private Long school_id;
	
	private Long fee_id;

	public Long getFee_id(){
		return fee_id;
	}
	
	public Long getSchool_id() {
		return school_id;
	}
	
	
	
}

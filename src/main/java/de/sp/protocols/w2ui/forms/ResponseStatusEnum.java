package de.sp.protocols.w2ui.forms;

public enum ResponseStatusEnum {
	
	success("success"), error("error");
	
	private String text;
	
	private ResponseStatusEnum(String text){
		this.text = text;
	}

	public String getText() {
		return text;
	}
	
	
	
}

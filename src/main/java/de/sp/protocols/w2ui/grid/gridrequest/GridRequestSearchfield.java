package de.sp.protocols.w2ui.grid.gridrequest;

public class GridRequestSearchfield {
	// { "field": "fname", "type": "text", "value": "vit", "operator": "is" }
	private String field;

	private String type;

	private String value;

	private String operator; // is, between

	public String getField() {
		return field;
	}

	public String getType() {
		return type;
	}

	public String getValue() {
		return value;
	}

	public GridRequestSearchfieldOperator getSearchfieldOperator(){
		return GridRequestSearchfieldOperator.valueOf(operator);
	}
	
}

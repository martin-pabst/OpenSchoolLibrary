package de.sp.protocols.w2ui.grid.gridrequest;

public class GridRequestSortfield {
 // { "field": "fname", "direction": "ASC" }
	private String field;
	
	private String direction;

	public String getField() {
		return field;
	}
	
	public GridRequestSortfieldDirection getDirection(){
		return GridRequestSortfieldDirection.valueOf(direction);
	}
}

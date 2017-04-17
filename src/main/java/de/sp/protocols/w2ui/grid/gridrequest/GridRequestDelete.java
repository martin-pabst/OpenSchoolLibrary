package de.sp.protocols.w2ui.grid.gridrequest;

import java.util.ArrayList;
import java.util.List;

public class GridRequestDelete {
	/*
	 * <code> "cmd": "get-records", "name": "myGrid", "limit": 50, "offset": 0,
	 * "selected": [1, 2], "search-logic": "AND", "search": [ { "field":
	 * "fname", "type": "text", "value": "vit", "operator": "is" }, { "field":
	 * "age", "type": "int", "value": [10, 20], "operator": "between" } ],
	 * "sort": { { "field": "fname", "direction": "ASC" }, { "field": "Lname",
	 * "direction": "DESC" } } </code>
	 */


	private String cmd;

	private String name;

	private List<Long> selected = new ArrayList<>();

	private Long school_id;

	public GridRequestDelete(){
		
	}
	
	public String getName() {
		return name;
	}

	public List<Long> getSelected() {
		return selected;
	}


	public GridRequestCommand getGridRequestCommand(){
		return GridRequestCommand.findByCommandText(cmd);
	}

	public String getCmd() {
		return cmd;
	}

	public Long getSchool_id() {
		return school_id;
	}
	
	
	
}

package de.sp.protocols.w2ui.grid.gridrequest;

import java.util.Map;

public class GridRequestSave {
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

	private Map<String, Object> record;
	
	private Long school_id;

	private Long school_term_id;

	
	public GridRequestSave(){
		
	}
	
	public String getName() {
		return name;
	}


	public GridRequestCommand getGridRequestCommand(){
		return GridRequestCommand.findByCommandText(cmd);
	}
	

	public Map<String, Object> getRecord(){
		return record;
	}

	public String getCmd() {
		return cmd;
	}

	public Long getSchool_id() {
		return school_id;
	}

	public Long getSchool_term_id() {
		return school_term_id;
	}


	
	
}

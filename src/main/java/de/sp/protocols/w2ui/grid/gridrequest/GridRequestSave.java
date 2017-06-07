package de.sp.protocols.w2ui.grid.gridrequest;

import de.sp.tools.validation.*;

import java.util.Map;

public class GridRequestSave implements SelfValidating{
	/*
	 * <code> "cmd": "get-records", "name": "myGrid", "limit": 50, "offset": 0,
	 * "selected": [1, 2], "search-logic": "AND", "search": [ { "field":
	 * "fname", "type": "text", "value": "vit", "operator": "is" }, { "field":
	 * "age", "type": "int", "value": [10, 20], "operator": "between" } ],
	 * "sort": { { "field": "fname", "direction": "ASC" }, { "field": "Lname",
	 * "direction": "DESC" } } </code>
	 */


	protected String cmd;

	protected String name;

	protected Map<String, Object> record;

	@Validation(notNull = true)
	protected Long school_id;

	protected Long school_term_id;

	
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


	@Override
	public void validate() throws ValidationException {
		if(record == null) return;

		record.forEach((key, value) -> {
			if(value instanceof  String){
				String stringValue = (String) value;
				String newValue = Validator.sanitize(stringValue, SanitizingStrategy.jsoupWhitelist);
				if(!value.equals(newValue)){
					record.put(key, newValue);
				}
			}

		});

	}
}

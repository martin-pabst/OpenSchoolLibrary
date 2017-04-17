package de.sp.tools.database.schema;

import org.simpleframework.xml.Attribute;

public class ReferencesSchema {
	
	@Attribute
	private String table;
	
	@Attribute
	private String column;
	

	public String getTable() {
		return table;
	}

	public String getColumn() {
		return column;
	}

}

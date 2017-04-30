package de.sp.tools.database.schema;

import org.simpleframework.xml.Attribute;

public class ReferencesSchema {
	
	@Attribute
	private String table;
	
	@Attribute
	private String column;

	@Attribute(required = false)
	private String ondelete;
	

	public String getTable() {
		return table;
	}

	public String getColumn() {
		return column;
	}

	public String getOndelete() {
		return ondelete;
	}
}

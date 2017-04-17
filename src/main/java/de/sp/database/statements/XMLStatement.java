package de.sp.database.statements;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Text;

public class XMLStatement {

	@Attribute(required = false)
	private String database;

	@Attribute
	private String name;

	@Text
	private String text;

	public String getDatabase() {
		return database;
	}

	public String getName() {
		return name;
	}

	public String getText() {
		return text;
	}

}

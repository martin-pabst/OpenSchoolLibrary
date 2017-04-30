package de.sp.database.statements;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

@Root(name = "statementlist")
public class XMLStatementList {

	@Attribute(required = false)
	private String database;

	@Attribute(required = false)
	private String name;

	@ElementList(inline = true, entry = "statement", required = false)
	private List<XMLStatement> statements;

	@ElementList(inline = true, entry = "statementlist", required = false)
	private List<XMLStatementList> statementLists;

	@Attribute(required = false)
	private String praefix;

	public List<XMLStatement> getStatements() {
		return statements;
	}

	public String getPraefix() {
		return praefix;
	}

	public String getDatabase() {
		return database;
	}

	public String getName() {
		return name;
	}

	public List<XMLStatementList> getStatementLists() {
		return statementLists;
	}
}

package de.sp.database.statements;

import java.util.List;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root(name = "statementlist")
public class XMLStatementList {

	@ElementList(inline = true, entry = "statement")
	private List<XMLStatement> statements;

	@Attribute(required = false)
	private String praefix;

	public List<XMLStatement> getStatements() {
		return statements;
	}

	public String getPraefix() {
		return praefix;
	}

}

package de.sp.database.statements;

import java.util.List;

public class SQLStatements {

	public String statement;

	public List<String> statements = null;

	public SQLStatements(String statement) {
		super();
		this.statement = statement;
	}

	public SQLStatements(List<String> statements) {
		this.statements = statements;
	}

	public List<String> getStatements() {
		return statements;
	}

	public String getStatement() {
		return statement;
	}

}

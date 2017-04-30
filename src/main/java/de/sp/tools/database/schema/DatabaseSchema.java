package de.sp.tools.database.schema;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.nio.file.Path;
import java.util.ArrayList;

@Root(name = "database")
public class DatabaseSchema {

	@Attribute
	private String name;

	@ElementList(inline = true)
	private ArrayList<TableSchema> tables;

	public ArrayList<String> toSQLStatements(DatabaseType databaseType)
			throws Exception {

		ArrayList<String> statements = new ArrayList<String>();

		// statements.add("create database " + name);

		if (tables != null) {
			for (TableSchema tableSchema : tables) {
				statements.add(tableSchema.toSQLStatement(databaseType));
			}
		}

		addReferencesStatements(statements, databaseType);

		addIndexStatements(statements, databaseType);

		return statements;

	}

	private void addIndexStatements(ArrayList<String> statements,
			DatabaseType databaseType) {

		if (tables != null) {
			for (TableSchema tableSchema : tables) {
				for (ColumnSchema columnSchema : tableSchema.getColumns()) {

					if (columnSchema.hasIndex()) {

						String columnName = columnSchema.getName();

						String statement = "create index idx_"
								+ tableSchema.getName() + "__" + columnName;

						statement += " on " + tableSchema.getName() + "("
								+ columnName + ")";

						statements.add(statement);
					}
				}
			}
		}

	}

	private void addReferencesStatements(ArrayList<String> statements,
			DatabaseType databaseType) {

		if (tables != null) {
			for (TableSchema tableSchema : tables) {
				for (ColumnSchema columnSchema : tableSchema.getColumns()) {
					ReferencesSchema rs = columnSchema.getReferences();

					if (rs != null) {

						String columnName = columnSchema.getName();

						if (columnName.endsWith("_id")) {
							columnName = columnName.substring(0,
									columnName.length() - 3);
						}

						String statement = "alter table "
								+ tableSchema.getName() + " add constraint fk_"
								+ tableSchema.getName() + "__" + columnName;

						statement += " foreign key (" + columnSchema.getName()
								+ ") references " + rs.getTable() + "("
								+ rs.getColumn() + ")";

						if(rs.getOndelete() != null && !rs.getOndelete().isEmpty()){
							statement += " on delete " + rs.getOndelete();
						}

						statements.add(statement);
					}
				}
			}
		}

	}

	@Override
	public String toString() {

		StringBuilder sb = new StringBuilder();

		try {
			for (String s : toSQLStatements(DatabaseType.postgres)) {
				sb.append(s);
				sb.append(";\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return sb.toString();
	}

	public ArrayList<TableSchema> getTables() {
		return tables;
	}

	public void writeTableJavaSourceToFiles(Path path) {
		for (TableSchema table : tables) {
			table.writeJavaCodeToFile(path);
		}
	}

	public void writeDAOSourceToFiles(Path path) {
		for (TableSchema table : tables) {
			table.writeDAOCodeToFile(path);
		}
	}

	public void writeStatementsToFiles(Path path) {
		for (TableSchema table : tables) {
			table.writeSQLStatementsToFile(path);
		}
	}

}

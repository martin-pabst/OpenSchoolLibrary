package de.sp.tools.database.schema;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.apache.commons.lang.StringUtils;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root(name = "table")
public class TableSchema {

	@Attribute
	private String name;

	@ElementList(inline = true)
	private ArrayList<ColumnSchema> columns;

	public String getName() {
		return name;
	}

	public ArrayList<ColumnSchema> getColumns() {
		return columns;
	}

	public String toSQLStatement(DatabaseType databaseType) throws Exception {

		String statement = "create table " + name + "(";

		for (int i = 0; i < columns.size(); i++) {

			ColumnSchema cs = columns.get(i);

			statement += cs.toSQLStatement(databaseType);

			if (i < columns.size() - 1) {
				statement += ", ";
			}
		}

		ArrayList<String> primaryKeys = new ArrayList<>();

		for (ColumnSchema cs : columns) {
			if (cs.isPrimarykey()) {
				primaryKeys.add(cs.getName());
			}
		}

		statement += ", primary key (" + StringUtils.join(primaryKeys, ",")
				+ ")";

		statement += ")";

		return statement;

	}

	public String getJavaSource() {

		StringBuilder sb = new StringBuilder();

		sb.append("package de.sp.database.model;\n");
		sb.append("import java.util.Date;\n");

		sb.append("\n\npublic class " + getJavaTablename() + "{\n\n");

		columns.forEach(column -> {
			sb.append("   private " + column.getJavaType() + " "
					+ column.getName() + ";\n\n");
		});

		sb.append("\n\n   public " + getJavaTablename() + "(){\n}\n\n"); // Parameterless
																			// constructor

		sb.append("\n\n   public " + getJavaTablename() + "("); // Constructor
																// with
																// parameters

		for (int i = 0; i < columns.size(); i++) {

			ColumnSchema column = columns.get(i);
			sb.append(column.getJavaType() + " " + column.getName());

			if (i < columns.size() - 1) {
				sb.append(", ");
			} else {
				sb.append("){\n\n");
			}

		}

		columns.forEach(column -> {
			sb.append("      this." + column.getName() + " = "
					+ column.getName() + ";\n");
		});

		sb.append("}\n\n");

		/*
		 * Getters
		 */
		columns.forEach(column -> {
			sb.append("   public " + column.getJavaType() + " get"
					+ firstCharUppercase(column.getName()) + "(){\n\n");
			sb.append("      return " + column.getName() + ";\n\n");
			sb.append("   }\n\n"); // close class
		});

		sb.append("}"); // close class

		return sb.toString();
	}

	public String getDAOSource() {
		StringBuilder sb = new StringBuilder();
		sb.append("package de.sp.database.daos.basic;\n\n");

		sb.append("import java.util.List;\n");
		sb.append("import org.sql2o.Connection;\n");
		sb.append("import de.sp.database.statements.StatementStore;\n\n");
		sb.append("import de.sp.database.model." + getJavaTablename() + ";\n\n");

		sb.append("public class " + getJavaTablename() + "DAO {\n");

		// getAll()-Method
		sb.append("	public static List<" + getJavaTablename()
				+ "> getAll(Connection con) {\n\n");

		sb.append("		String sql = StatementStore.getStatement(\"" + name
				+ ".getAll\");\n");

		sb.append("		List<" + getJavaTablename() + "> " + name
				+ "list = con.createQuery(sql).executeAndFetch(\n");
		sb.append("				" + getJavaTablename() + ".class);\n");

		sb.append("		return " + name + "list;\n\n");

		sb.append("	}\n\n");

		// insert-Method
		sb.append("public static " + getJavaTablename() + " insert(");

		for (int i = 0; i < columns.size(); i++) {

			ColumnSchema column = columns.get(i);

			if (!column.getName().equals("id")) {

				sb.append(column.getJavaType() + " " + column.getName());

				if (i < columns.size() - 1) {
					sb.append(", ");
				}
			}

		}

		sb.append(", Connection con) throws Exception {\n\n");

		sb.append("String sql = StatementStore.getStatement(\"" + name
				+ ".insert\");\n\n");

		sb.append("Long id = con.createQuery(sql, true)\n");

		columns.forEach(column -> {
			if (!column.getName().equals("id")) {
				sb.append(".addParameter(\"" + column.getName() + "\", "
						+ column.getName() + ")\n");
			}
		});

		sb.append(".executeUpdate()\n");
		sb.append(".getKey(Long.class);\n\n");

		sb.append("return new " + getJavaTablename() + "(");

		for (int i = 0; i < columns.size(); i++) {

			ColumnSchema column = columns.get(i);

			sb.append(column.getName());

			if (i < columns.size() - 1) {
				sb.append(", ");
			} else {
				sb.append(");\n\n");
			}

		}

		sb.append("}\n\n");

		// update-Method
		sb.append("public static void update(" + getJavaTablename() + " "
				+ name + ", Connection con) throws Exception {\n\n");

		sb.append("   String sql = StatementStore.getStatement(\"" + name
				+ ".update\");\n\n");

		sb.append("   con.createQuery(sql)\n");

		columns.forEach(column -> {
			sb.append("      .addParameter(\"" + column.getName() + "\", "
					+ name + ".get" + firstCharUppercase(column.getName())
					+ "())\n");
		});

		sb.append("   .executeUpdate();\n");

		sb.append("}\n\n");

		// Delete-statement

		sb.append("public static void delete(Long id, Connection con){\n\n");

		sb.append("String sql = StatementStore.getStatement(\"" + name
				+ ".delete\");\n\n");

		sb.append("con.createQuery(sql)\n\n");
		sb.append(".addParameter(\"id\", id)\n\n");
		sb.append(".executeUpdate();\n\n");

		sb.append("}\n\n");
		
		// findById()-Method
		sb.append("	public static " + getJavaTablename()
				+ " findById(Long id, Connection con) {\n\n");

		sb.append("		String sql = StatementStore.getStatement(\"" + name
				+ ".findById\");\n");

		sb.append("		" + getJavaTablename() + " " + name
				+ " = con.createQuery(sql).executeAndFetchFirst(\n");
		sb.append("				" + getJavaTablename() + ".class);\n");

		sb.append("		return " + name + ";\n\n");

		sb.append("	}\n\n");

		

		sb.append("}\n\n");

		return sb.toString();
	}

	public String getStatements() {

		StringBuilder sb = new StringBuilder();
		sb.append("		<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n\n");

		sb.append("		<statementlist praefix=\"" + getName() + "\">\n\n");

		// getAll
		sb.append("		<statement name=\"getAll\">\n");
		sb.append("        select * from " + name + "\n");
		sb.append("     </statement>\n\n");

		// insert
		sb.append("		<statement name=\"insert\">\n");
		sb.append("        insert into " + name + "(");

		for (int i = 0; i < columns.size(); i++) {

			ColumnSchema column = columns.get(i);

			if (!column.getName().equals("id")) {

				sb.append(column.getName());

				if (i < columns.size() - 1) {
					sb.append(", ");
				}
			}

		}

		sb.append(")\n");

		sb.append("        values (");

		for (int i = 0; i < columns.size(); i++) {

			ColumnSchema column = columns.get(i);

			if (!column.getName().equals("id")) {

				sb.append(":" + column.getName());

				if (i < columns.size() - 1) {
					sb.append(", ");
				}
			}

		}

		sb.append("        )\n");
		sb.append("     </statement>\n\n");

		// update
		sb.append("		<statement name=\"update\">\n\n");
		sb.append("        update " + name + " set \n");

		for (int i = 0; i < columns.size(); i++) {

			ColumnSchema column = columns.get(i);

			if (!column.getName().equals("id")) {

				sb.append("           " + column.getName() + " = :"
						+ column.getName());

				if (i < columns.size() - 1) {
					sb.append(",\n");
				}
			}

		}

		sb.append("\n where id = :id\n");

		sb.append("     </statement>\n\n");

		// delete
		sb.append("		<statement name=\"delete\">\n");
		sb.append("        delete from " + name + "\n");
		sb.append("        where id = :id\n");
		sb.append("     </statement>\n\n");

		// findById
		sb.append("		<statement name=\"findById\">\n");
		sb.append("        select * from " + name + "\n");
		sb.append("        where id = :id\n");
		sb.append("     </statement>\n\n");

		
		sb.append("     </statementlist>\n\n");

		return sb.toString();
	}

	public String getJavaTablename() {

		String javaName = "";

		for (int i = 0; i < name.length(); i++) {

			char c = name.charAt(i);

			if (c == '_') {
				i++;
				if (i < name.length()) {
					javaName += ("" + name.charAt(i)).toUpperCase();
				}
			} else {
				javaName += c;
			}

		}

		return firstCharUppercase(javaName);

	}

	private String firstCharUppercase(String s) {
		if (s != null && !s.isEmpty()) {
			return ("" + s.charAt(0)).toUpperCase() + s.substring(1);
		} else {
			return s;
		}
	}

	public void writeJavaCodeToFile(Path path) {
		try {
			Path file = Paths
					.get(path.toString(), getJavaTablename() + ".java");

			Files.write(file, getJavaSource().getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void writeDAOCodeToFile(Path path) {
		try {
			Path file = Paths.get(path.toString(), getJavaTablename()
					+ "DAO.java");

			Files.write(file, getDAOSource().getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void writeSQLStatementsToFile(Path path) {
		try {
			Path file = Paths.get(path.toString(), name + ".xml");

			Files.write(file, getStatements().getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}

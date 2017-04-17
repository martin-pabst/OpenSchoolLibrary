package de.sp.database.statements;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import de.sp.tools.file.FileTool;

public class StatementStore {

	private static HashMap<String, SQLStatement> statementMap = new HashMap<String, SQLStatement>();

	public static String getStatement(String name) {
		return statementMap.get(name).getStatement();
	}

	public static void readStatements(String databaseType) throws Exception {

		List<String> filenames = FileTool
				.listAllResourceFiles("/database/statements");

		for (String filename : filenames) {

			Serializer serializer = new Persister();

			// This works inside jarfile ...
			InputStream in = StatementStore.class.getResourceAsStream(filename);

			if (in == null) {

				// ... this works outside jarfile
				in = StatementStore.class.getClassLoader().getResourceAsStream(
						filename);

			}

			XMLStatementList statementList = serializer.read(
					XMLStatementList.class, in);

			// File file = new File(filename);
			//
			// XMLStatementList statementList =
			// serializer.read(XMLStatementList.class, file);
			
			String praefix = statementList.getPraefix();
			
			if(praefix == null){
				praefix = "";
			} else {
				if(!praefix.endsWith(".")){
					praefix += ".";
				}
			}

			for (XMLStatement statement : statementList.getStatements()) {
				if (statement.getDatabase() == null
						|| statement.getDatabase().equalsIgnoreCase(
								databaseType)) {
					statementMap.put(praefix + statement.getName(), new SQLStatement(
							statement.getText()));
				}
			}

		}

	}

}

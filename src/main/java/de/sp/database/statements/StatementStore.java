package de.sp.database.statements;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
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


	public static void readStatements(String databaseType, String fileystemPath) throws Exception {

	    List<Path> paths = FileTool.listFiles(Paths.get(fileystemPath), ".xml");

        ArrayList<String> pathStrings = new ArrayList<>();

        for(Path path: paths){

            FileInputStream fis = new FileInputStream(path.toFile());
            registerStatements(databaseType, fis);
        }

    }

	public static void readStatements(String databaseType) throws Exception {

		List<String> filenames = FileTool
				.listAllResourceFiles("/database/statements");

        readStatements(databaseType, filenames);

    }

    private static void readStatements(String databaseType, List<String> filenames) throws Exception {
        for (String filename : filenames) {


            // This works inside jarfile ...
            InputStream in = StatementStore.class.getResourceAsStream(filename);

            if (in == null) {

                // ... this works outside jarfile
                in = StatementStore.class.getClassLoader().getResourceAsStream(
                        filename);

            }

            registerStatements(databaseType, in);

        }
    }

    private static void registerStatements(String databaseType, InputStream in) throws Exception {
        Serializer serializer = new Persister();
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

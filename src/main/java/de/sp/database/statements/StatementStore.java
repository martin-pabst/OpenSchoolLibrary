package de.sp.database.statements;

import de.sp.tools.file.FileTool;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class StatementStore {

    private static HashMap<String, SQLStatements> statementMap = new HashMap<String, SQLStatements>();

    public static String getStatement(String name) {
        return statementMap.get(name).getStatement();
    }

    public static List<String> getStatements(String name) {
        return statementMap.get(name).getStatements();
    }

    public static void readStatements(String databaseType, String fileystemPath) throws Exception {

        List<Path> paths = FileTool.listFiles(Paths.get(fileystemPath), ".xml");

        ArrayList<String> pathStrings = new ArrayList<>();

        for (Path path : paths) {

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

        if (praefix == null) {
            praefix = "";
        } else {
            if (!praefix.endsWith(".")) {
                praefix += ".";
            }
        }

        if (statementList.getStatements() != null) {
            for (XMLStatement statement : statementList.getStatements()) {
                if (statement.getDatabase() == null
                        || statement.getDatabase().equalsIgnoreCase(
                        databaseType)) {

                    statementMap.put(praefix + statement.getName(), new SQLStatements(
                            statement.getText()));

                }
            }
        }

        if (statementList.getStatementLists() != null) {
            for (XMLStatementList statementList1 : statementList.getStatementLists()) {
                if (statementList1.getDatabase() == null
                        || statementList1.getDatabase().equalsIgnoreCase(
                        databaseType)) {

                    List<String> statements = new ArrayList<>();

                    for (XMLStatement xmlStatement : statementList1.getStatements()) {
                        statements.add(xmlStatement.getText());
                    }

                    statementMap.put(praefix + statementList1.getName(), new SQLStatements(
                            statements));

                }
            }
        }
    }

}

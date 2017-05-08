package de.sp.asvsst.database;

import de.sp.asvsst.ASVXMLParser;
import de.sp.asvsst.databasewriter.ASVToDatabaseWriter;
import de.sp.asvsst.model.ASVExport;
import de.sp.database.connection.ConnectionPool;
import de.sp.database.model.StoreManager;
import de.sp.database.statements.StatementStore;
import de.sp.main.config.Configuration;
import de.sp.main.resources.modules.ModuleManager;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by martin on 24.04.2017.
 */
public class InsertTestdata {

    public static void main(String[] args) throws Exception {

        Configuration config = Configuration.getInstance();

        ConnectionPool.init(config);

        // read sql-statements from xml files
        StatementStore.readStatements((String) config.getDatabase().getType(),
                (String) "./src/main/resources/database/statements");

        StatementStore.readStatements((String) config.getDatabase().getType(),
                (String) "./ASV_Schnittstelle/src/main/resources/database/statements");

        try {
            StoreManager.getInstance().loadStoresFromDatabase(
                    ModuleManager.getAllPermissions());

        // Schuljahr 2016/17

        String filename = "ASV_Schnittstelle/Testdaten/ASV-Export 24.04.2017.zip";
        String password = "0x8E0vEe";

        importFromASV(filename, password);


        // Schuljahr 2017/18
        importFromASV("ASV_Schnittstelle/Testdaten/ASV-Export 2017,18 (8.5.17).zip", "?+QGcQRS");

        } catch (Exception ex) {

        }

    }

    private static void importFromASV(String filename, String password) {
        ASVToDatabaseWriter asvToDatabaseWriter = new ASVToDatabaseWriter(null);

        ASVXMLParser importer = new ASVXMLParser();

        Path path = Paths.get(filename);

        ASVExport asvExport = importer.parseASVXMLData(
               path.toString(), password, null);

        asvToDatabaseWriter.writeToDatabase(asvExport);
    }


}

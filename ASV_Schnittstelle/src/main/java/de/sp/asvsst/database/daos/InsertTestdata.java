package de.sp.asvsst.database.daos;

import de.sp.asvsst.ASVXMLParser;
import de.sp.asvsst.databasewriter.ASVToDatabaseWriter;
import de.sp.asvsst.model.ASVExport;
import de.sp.database.connection.ConnectionPool;
import de.sp.database.model.StoreManager;
import de.sp.database.statements.StatementStore;
import de.sp.main.config.Configuration;
import de.sp.main.resources.modules.ModuleManager;
import de.sp.main.resources.modules.Permission;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by martin on 24.04.2017.
 */
public class InsertTestdata {

    public static void main(String[] args) throws Exception {

        Configuration config = Configuration.getInstance();

        ConnectionPool.init(config);

        // read sql-statements from xml files
        StatementStore.readStatements((String) config.getDatabase().getType(),
                (String) "C:/Vorhaben/IntelliJ/OpenSchoolPlatform/src/main/resources/database/statements");

        StatementStore.readStatements((String) config.getDatabase().getType(),
                (String) "C:/Vorhaben/IntelliJ/OpenSchoolPlatform/ASV_Schnittstelle/src/main/resources/database/statements");

        try {
            StoreManager.getInstance().loadStoresFromDatabase(
                    ModuleManager.getAllPermissions());
        } catch (Exception ex) {

        }

        ASVToDatabaseWriter asvToDatabaseWriter = new ASVToDatabaseWriter(null);

        ASVXMLParser importer = new ASVXMLParser();

        final Path path = Paths.get("ASV_Schnittstelle/Testdaten/ASV-Export 24.04.2017.zip");

        final ASVExport asvExport = importer.parseASVXMLData(
                path.toString(), "0x8E0vEe", null);

        asvToDatabaseWriter.writeToDatabase(asvExport);

    }
    
    
    
    
}

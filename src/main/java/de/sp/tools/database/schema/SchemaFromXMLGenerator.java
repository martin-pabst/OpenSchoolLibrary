package de.sp.tools.database.schema;

import java.io.InputStream;
import java.nio.file.Path;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

public class SchemaFromXMLGenerator {

	private DatabaseSchema schema;

	public void readFromFile(String filename) throws Exception {

		Serializer serializer = new Persister();
		// e.g. "database/databaseschema.xml
		InputStream is = ClassLoader.class.getResourceAsStream(filename);

		schema = serializer.read(DatabaseSchema.class, is);

	}

	@Override
	public String toString() {

		if (schema == null) {
			return "No database schema present.";
		} else {
			return schema.toString();
		}

	}

	public DatabaseSchema getSchema() {
		return schema;
	}

	public void writeTableJavaSourceToFiles(Path path){
		schema.writeTableJavaSourceToFiles(path);
	}
	
	public void writeDAOJavaSourceToFiles(Path path){
		schema.writeDAOSourceToFiles(path);
	}
	
	public void writeStatementsToFiles(Path path){
		schema.writeStatementsToFiles(path);
	}
	
}

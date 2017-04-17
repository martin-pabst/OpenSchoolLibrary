package de.sp.tools.database.schema;

public enum DDLTypeEnum {

	varchar("varchar", "varchar", "String"), // one parameter
	integer("integer", "int", "Integer"), //
	bigint("bigint", "bigint", "Long"), //
	decimal("decimal", "decimal", "Double"), // two parameters
	text("text", "longtext", "String"), //
	time("time", "time", "Date"), //
	date("date", "date", "Date"), //
	timestamp("timestamp", "timestamp", "Date"), //
	bool("boolean", "boolean", "Boolean"), //
	blob("bytea", "longblob", "byte[]");
	
	private String postgresTypename;
	private String mySQLTypename;
	private String javaTypename;

	private DDLTypeEnum(String postgresTypename, String mySQLTypename, String javaTypename) {
		this.postgresTypename = postgresTypename;
		this.mySQLTypename = mySQLTypename;
		this.javaTypename = javaTypename;
	}

	public String getPostgresTypename() {
		return postgresTypename;
	}

	public String getMySQLTypename() {
		return mySQLTypename;
	}

	public String getJavaTypename() {
		return javaTypename;
	}

	public String getName(DatabaseType databaseType) throws Exception {
		
		switch (databaseType) {
		case postgres:
			return postgresTypename;
		case mysql:
			return mySQLTypename;
		}

		throw new Exception("Unknown typename for given databasetype "
				+ databaseType == null ? "null" : databaseType.toString());
	}
}

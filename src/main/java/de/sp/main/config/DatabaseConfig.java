package de.sp.main.config;

import de.sp.tools.database.schema.DatabaseType;

public class DatabaseConfig {
	
	private String type;
	private String connection;
	private String username;
	private String password;

	public String getType() {
		return type;
	}
	
	public DatabaseType getDatabaseType(){
		if(type == null){
			return null;
		}
		
		return DatabaseType.valueOf(type);
	}
	
	
	public String getDriverClassname() throws Exception{
		
		if(type.equalsIgnoreCase("postgres")){
			return "org.postgresql.Driver";
		}
		
		if(type.equalsIgnoreCase("mysql")){
			return "com.mysql.jdbc.Driver";
		}
		
		throw new Exception("Databasetype " + type + " not known.");
	}

	public String getConnection() {
		return connection;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

}

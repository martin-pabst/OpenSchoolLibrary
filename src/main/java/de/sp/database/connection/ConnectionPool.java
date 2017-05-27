package de.sp.database.connection;

import com.jolbox.bonecp.BoneCPConfig;
import com.jolbox.bonecp.BoneCPDataSource;
import de.sp.main.config.Configuration;
import de.sp.main.config.DatabaseConfig;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import org.sql2o.quirks.PostgresQuirks;

import java.util.concurrent.TimeUnit;

public class ConnectionPool {
	
	private static BoneCPDataSource boneCPDataSource;
	private static DatabaseConfig dbConfig;
	
	public static void init (Configuration config)
			throws Exception {

		dbConfig = config.getDatabase();

		Class.forName(dbConfig.getDriverClassname());

		BoneCPConfig bcConfig = new BoneCPConfig();

		bcConfig.setJdbcUrl(dbConfig.getConnection());

		bcConfig.setUsername(dbConfig.getUsername());

		bcConfig.setPassword(dbConfig.getPassword());

		bcConfig.setMinConnectionsPerPartition(5);

		bcConfig.setMaxConnectionsPerPartition(40);

		bcConfig.setPartitionCount(1);

		bcConfig.setLazyInit(true);
		bcConfig.setAcquireRetryAttempts(5);
		bcConfig.setAcquireRetryDelay(10, TimeUnit.SECONDS);

		boneCPDataSource = new BoneCPDataSource(bcConfig);


	}
	
	public static Sql2o getSQL2o(){
		
		if(dbConfig.getType().equalsIgnoreCase("postgres")){
			return new Sql2o(boneCPDataSource, new PostgresQuirks());			
		} else {
			return new Sql2o(boneCPDataSource);
		}
		
		
	}
	
	public static Connection open(){
		return getSQL2o().open();
	}
	
	public static void close(){
		boneCPDataSource.close();
	}
	
	public static Connection beginTransaction(){
		return getSQL2o().beginTransaction();
	}
	
	
}

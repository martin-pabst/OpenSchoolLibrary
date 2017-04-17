package de.sp.database.setup;

import org.sql2o.Connection;

import de.sp.database.connection.ConnectionPool;
import de.sp.main.config.Configuration;

public class DatabaseTester {

	public static DatabaseStateEnum test() {

		Configuration config = Configuration.getInstance();

		try {
			
			ConnectionPool.init(config);
			
			Integer size = null;
			
			try(Connection con = ConnectionPool.open()){

				size = con.createQuery("select count(*) from school").executeScalar(Integer.class);
				
			} catch (Exception ex){
				return DatabaseStateEnum.databaseEmpty;
			}
			
			if(size == null || size < 1){
				return DatabaseStateEnum.databaseEmpty;
			}
			
			return DatabaseStateEnum.databaseNotEmpty;
			
		} catch (Exception e) {

			return DatabaseStateEnum.connectionError;

		}

	}

}

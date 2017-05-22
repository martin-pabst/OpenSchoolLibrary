package de.sp.database.model;

import de.sp.database.stores.*;
import de.sp.main.resources.modules.Permission;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class StoreManager {
	
	private static StoreManager instance;
	
	
	public static StoreManager getInstance(){
		
		if(instance == null){
			
			instance = new StoreManager();
			
		}
		
		return instance;
	}
	
	public void loadStoresFromDatabase(List<Permission> permissions){

		Logger logger = LoggerFactory.getLogger(StoreManager.class);
		logger.info("Reading cached objects from database...");
		long timeMilliseonds = System.currentTimeMillis();


		SchoolTermStore.getInstance().loadFromDatabase();
		
		UserRolePermissionStore.getInstance().addPermissions(permissions);

		UserRolePermissionStore.getInstance().loadFromDatabase();

		ValueListStore.getInstance().loadFromDatabase();

		SubjectStore.getInstance().loadFromDatabase();

		EventStore.getInstance().loadFromDatabase();

		StudentClassStore.getInstance().loadFromDatabase();

		timeMilliseonds = System.currentTimeMillis() - timeMilliseonds;

		logger.info("Finished reading cached objects in " + timeMilliseonds + " ms.");
		
	}
	
}

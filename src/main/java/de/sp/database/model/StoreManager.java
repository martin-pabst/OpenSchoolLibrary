package de.sp.database.model;

import de.sp.database.stores.*;
import de.sp.main.resources.modules.ModuleManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class StoreManager {
	
	private static StoreManager instance;

	private static List<DatabaseStore> stores = new ArrayList<>();
	
	public static StoreManager getInstance(){
		
		if(instance == null){
			
			instance = new StoreManager();

			stores.add(SchoolTermStore.getInstance());
			stores.add(ValueListStore.getInstance());
			stores.add(UserRolePermissionStore.getInstance());
			stores.add(SubjectStore.getInstance());
			stores.add(StudentClassStore.getInstance());
			stores.add(EventStore.getInstance());

		}
		
		return instance;
	}

	public void removeSchoolFromStores(Long school_id){
		for (int i = stores.size() - 1; i >= 0; i--) {
			DatabaseStore store = stores.get(i);
			store.removeSchool(school_id);
		}
	}

	public void loadStoresFromDatabase(){

		Logger logger = LoggerFactory.getLogger(StoreManager.class);
		logger.info("Reading cached objects from database...");
		long timeMilliseonds = System.currentTimeMillis();

		UserRolePermissionStore.getInstance().addPermissions(ModuleManager.getAllPermissions());

		for (DatabaseStore store : stores) {
			store.loadFromDatabase();
		}

		timeMilliseonds = System.currentTimeMillis() - timeMilliseonds;

		logger.info("Finished reading cached objects in " + timeMilliseonds + " ms.");
		
	}
	
}

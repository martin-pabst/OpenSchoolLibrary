package de.sp.database.model;

import de.sp.database.stores.CalendarStore;
import de.sp.database.stores.SchoolTermStore;
import de.sp.database.stores.UserRolePermissionStore;
import de.sp.database.stores.ValueListStore;
import de.sp.main.resources.modules.Permission;

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
		
		SchoolTermStore.getInstance().loadFromDatabase();
		
		UserRolePermissionStore.getInstance().addPermissions(permissions);
		UserRolePermissionStore.getInstance().loadFromDatabase();

		ValueListStore.getInstance().loadFromDatabase();

		CalendarStore.getInstance().loadFromDatabase();
		
		
	}
	
}

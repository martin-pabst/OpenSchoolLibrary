package de.sp.database.model;


public class PermissionDB {
	
	private long id;
	
	private String name;
	

	public PermissionDB(long id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

}

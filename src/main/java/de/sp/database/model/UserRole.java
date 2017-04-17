package de.sp.database.model;


public class UserRole {
	
	public long id;
	
	public long user_id;
	
	public long role_id;

	public UserRole(long id, long user_id, long role_id) {
		super();
		this.id = id;
		this.user_id = user_id;
		this.role_id = role_id;
	}
	
	
	
}

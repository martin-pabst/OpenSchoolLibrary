package de.sp.main.resources.modules;

import de.sp.database.model.Role;

public class Permission {
	
	private String name;
	
	private String remark;

	public Permission(String name, String remark) {
		super();
		this.name = name;
		this.remark = remark;
	}

	public String getName() {
		return name;
	}

	public String getRemark() {
		return remark;
	}

	/**
	 * Don't alter this implementation {@link Role.adPermission} depends on it.
	 */
	@Override
	public String toString() {
		return name;
	}
	
	
	
	
}

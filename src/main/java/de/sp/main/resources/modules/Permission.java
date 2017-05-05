package de.sp.main.resources.modules;

import de.sp.database.model.Role;

public class Permission {

	/*
		As w2grid can't cope with nonnumeric recids, we have to generate artificial ids:
	 */
	private static long maxId = 1;

	private String name;
	
	private String remark;

	private long id;

	public Permission(String name, String remark) {
		super();
		this.name = name;
		this.remark = remark;
		id = maxId++;
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

	public long getId() {
		return id;
	}
}

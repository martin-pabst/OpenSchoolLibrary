package de.sp.database.model;

import de.sp.main.resources.modules.Permission;
import org.apache.commons.lang.StringUtils;
import org.simpleframework.xml.Transient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Role {

	private long id;

	private String name;

	private String remark;

	private long school_id;

	private String permissions;

	@Transient
	private School school;

	@Transient
	private ArrayList<Permission> permissionList = new ArrayList<>();
	
	@Transient
	private HashMap<String, Permission> permissionNameMap = new HashMap<>();

	/**
	 * We need this parameterless constructor as only this one is called by
	 * SQL2o. Without this call roleMap and permissionMap doesn't get initialized.
	 */
	public Role(){
		
	}
	
	public Role(long id, String name, String remark, long school_id,
			String permissions) {
		super();
		this.id = id;
		this.name = name;
		this.remark = remark;
		this.school_id = school_id;
		this.permissions = permissions;
	}

	public void addPermission(Permission permission) {
		if (permissionNameMap.get(permission.getName()) == null) {
			
			permissionList.add(permission);
			permissionNameMap.put(permission.getName(), permission);
			
			permissions = StringUtils.join(permissionList, "|");
						
		}
	}

	public void removePermission(Permission permission) {
		permissionList.remove(permission);
		permissionNameMap.remove(permission.getName());
		permissions = StringUtils.join(permissionList, "|");
	}

	public ArrayList<Permission> getPermissionList() {
		return permissionList;
	}

	public String getPermissions() {
		return permissions;
	}

	public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getRemark() {
		return remark;
	}

	public long getSchool_id() {
		return school_id;
	}

	public School getSchool() {
		return school;
	}

	public void setSchool(School school) {
		this.school = school;
	}

	public void registerPermissions(Map<String, Permission> allPermissionNameMap) {

		String[] permissionNames = StringUtils.split(permissions, "|");
		
		for(String permissionName: permissionNames){
			Permission p = allPermissionNameMap.get(permissionName);
			if(p != null){
				if (permissionNameMap.get(p.getName()) == null) {
					permissionList.add(p);
					permissionNameMap.put(p.getName(), p);
				}
			}
		}
		
	
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
}

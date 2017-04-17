package de.sp.database.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.sp.database.stores.SchoolTermStore;
import de.sp.main.resources.modules.InsufficientPermissionException;
import de.sp.main.resources.modules.Permission;
import de.sp.modules.admin.AdminModule;

public class User {

	private long id;
	private String username;
	private String name;
	private String hash;
	private String salt;
	private String languageCode;
	private Long last_selected_school_term_id = null;
	private Boolean is_admin = false;

	private HashMap<School, List<Role>> roleMap = new HashMap<>();

	private HashMap<School, Set<String>> permissionMap = new HashMap<>();

	/**
	 * We need this parameterless constructor as only this one is called by
	 * SQL2o. Without this call roleMap and permissionMap doesn't get
	 * initialized.
	 */
	public User() {

	}

	public User(long id, String username, String name, String hash,
			String salt, String languageCode, boolean isAdmin) {
		super();
		this.id = id;
		this.username = username;
		this.name = name;
		this.hash = hash;
		this.salt = salt;
		this.languageCode = languageCode;
		this.is_admin = isAdmin;
	}

	public long getId() {
		return id;
	}

	public String getUsername() {
		return username;
	}

	public String getName() {
		return name;
	}

	public String getHash() {
		return hash;
	}

	public String getSalt() {
		return salt;
	}

	public String getLanguageCode() {
		return languageCode;
	}

	
	public Boolean is_admin() {
		return is_admin;
	}

	public List<Role> getRoles(School school) {

		List<Role> roles = roleMap.get(school);

		if (roleMap.get(school) != null) {

			return roles;

		}

		return new ArrayList<Role>();

	}

	public void addRole(Role role) {

		School school = role.getSchool();

		List<Role> roles = roleMap.get(school);

		if (roles == null) {
			roles = new ArrayList<>();
			roleMap.put(school, roles);
		}

		roles.add(role);

		Set<String> permissions = permissionMap.get(school);

		if (permissions == null) {
			permissions = new HashSet<>();
			permissionMap.put(school, permissions);
		}

		for (Permission p : role.getPermissions()) {

			String name = p.getName();

			while (name.length() > 0) {

				permissions.add(p.getName());

				int i = name.lastIndexOf(".");

				if (i > 0) {
					name = name.substring(0, i);
				} else {
					name = "";
				}

			}

		}

	}

	public boolean hasPermission(String permission, Long school_id) {

		if(is_admin && permission.startsWith(AdminModule.PERMISSIONADMIN)){
			return true;
		}
		
		if(school_id == null){
			return false;
		}
		
		School school = SchoolTermStore.getInstance().getSchoolById(school_id);

		return hasPermission(permission, school);

	}

	public boolean hasPermission(String permission, School school) {

		if(is_admin && permission.startsWith(AdminModule.PERMISSIONADMIN)){
			return true;
		}
		
		if (school == null) {
			return false;
		}

		if (permission == null) {
			return true;
		}

		return permissionMap.get(school).contains(permission);
	}

	public SchoolTerm getLastSelectedSchoolTerm() {

		if (last_selected_school_term_id == null) {
			return null;
		}

		return SchoolTermStore.getInstance().getSchoolTerm(last_selected_school_term_id);
	}
	
	public void setLast_selected_school_term_id(Long last_selected_school_term_id) {
		this.last_selected_school_term_id = last_selected_school_term_id;
	}

	public Long getLast_selected_school_term_id() {
		return last_selected_school_term_id;
	}

	public void setLastSelectedSchoolTermId(Long school_term_id) {
		last_selected_school_term_id = school_term_id;
	}

	public void setLastSelectedSchoolTermIfNull() {

		if(getLastSelectedSchoolTerm() == null){

			School[] schools = roleMap.keySet().toArray(new School[0]);

			if (schools.length > 0) {
				School school = schools[0];
				SchoolTerm st = school.getCurrentSchoolTerm();
				if(st != null){
					last_selected_school_term_id = st.getId();
				}
			}
			
		}

	}

	public List<School> getSchools() {

		School[] schools = roleMap.keySet().toArray(new School[0]);

		ArrayList<School> schoolList = new ArrayList<School>();

		for (School school : schools) {
			schoolList.add(school);
		}

		return schoolList;
	}

	public boolean hasAnyPermissionForSchool(School school) {
		return school == null || permissionMap.get(school) != null;
	}

	public void checkPermission(String permission, Long school_id)
			throws InsufficientPermissionException {

		if (hasPermission(permission, school_id)) {
			return;
		} else {

			School school = SchoolTermStore.getInstance().getSchoolById(
					school_id);

			throw new InsufficientPermissionException(this, permission, school);

		}

	}

	public void checkPermission(String permission, School school)
			throws InsufficientPermissionException {

		if (hasPermission(permission, school)) {
			return;
		} else {

			throw new InsufficientPermissionException(this, permission, school);

		}

	}

}

package de.sp.database.stores;

import de.sp.database.connection.ConnectionPool;
import de.sp.database.daos.basic.RoleDAO;
import de.sp.database.daos.basic.UserDAO;
import de.sp.database.daos.basic.UserRoleDAO;
import de.sp.database.model.Role;
import de.sp.database.model.School;
import de.sp.database.model.User;
import de.sp.main.resources.modules.Permission;
import org.sql2o.Connection;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 
 * Stores Schools, Users, Roles and Permissions
 * 
 * @author M. Pabst, 2016
 *
 */

public class UserRolePermissionStore {

	private static UserRolePermissionStore instance;

	private Map<Long, User> userKeys = new ConcurrentHashMap<>(); // maps User.id to User
	private Map<Long, Map<String, User>> schoolIdToUserNameMap = new ConcurrentHashMap<>();

	private Map<Long, List<Role>> schoolIdToRoleListMap = new ConcurrentHashMap<>();
	private Map<Long, Role> roleKeys = new ConcurrentHashMap<>(); // maps Role.id to role

	private Map<String, Permission> permissionNameMap = new ConcurrentHashMap<>();

	public static UserRolePermissionStore getInstance() {

		if (instance == null) {
			instance = new UserRolePermissionStore();
		}

		return instance;

	}

	public void loadFromDatabase() {

		try (Connection con = ConnectionPool.open()) {

			UserDAO.getAll(con).forEach(user -> addUser(user));
			RoleDAO.getAll(con).forEach(role -> {
				addRole(role);
				role.registerPermissions(permissionNameMap);
			});

			Map<Long, School> schoolKeys = SchoolTermStore.getInstance()
					.getSchoolKeys();

			UserRoleDAO.joinUsersWithRoles(userKeys, roleKeys, schoolKeys, con);
		}

	}

	public void addRole(Role role) {

		List<Role> roleList = schoolIdToRoleListMap.get(role.getSchool_id());
		if(roleList == null){
			roleList = new CopyOnWriteArrayList<>();
			schoolIdToRoleListMap.put(role.getSchool_id(), roleList);
		}
		roleList.add(role);

		roleKeys.put(role.getId(), role);
		role.registerPermissions(permissionNameMap);

		Map<Long, School> schoolKeys = SchoolTermStore.getInstance()
				.getSchoolKeys();

		role.setSchool(schoolKeys.get(role.getSchool_id()));
	}

	public void addUser(User user) {

		userKeys.put(user.getId(), user);

		Map<String, User> userNameMap = schoolIdToUserNameMap.get(user.getSchool_id());

		if(userNameMap == null){
			userNameMap = new ConcurrentHashMap<>();
			schoolIdToUserNameMap.put(user.getSchool_id(), userNameMap);
		}

		userNameMap.put(user.getUsername(), user);

	}

	public void addPermission(Permission permission) {
		if (permissionNameMap.get(permission.getName()) == null) {
			permissionNameMap.put(permission.getName(), permission);
		}
	}

	public void addPermissions(List<Permission> permissionsList) {

		permissionsList.forEach(permission -> addPermission(permission));

	}

	public User getUserBySchoolIdAndName(Long school_id, String username) {

		Map<String, User> userNameMap = schoolIdToUserNameMap.get(school_id);

		if(userNameMap == null){
			return null;
		}

		return userNameMap.get(username);

	}

	public User getUserById(Long user_id) {

		return userKeys.get(user_id);

	}

	public Role getRoleById(Long role_id) {

		return roleKeys.get(role_id);

	}

	public void addRoleToUser(Role role, User user, Connection con) {

		UserRoleDAO.addRoleToUser(user.getId(), role.getId(), con);

		user.addRole(role);

	}

	public void removeRoleFromUser(Role role, User user, Connection con) {

		UserRoleDAO.removeRoleFromUser(user.getId(), role.getId(), con);
		user.removeRole(role);

	}

    public void removeUsers(List<User> usersToRemove, Connection con) {

		/*
		 * If something goes wrong in database operations
		 * we don't want to update object model. Therefore
		 * we do two separate iterations over List usersToRemove.
		 */

		for (User user : usersToRemove) {
			UserDAO.deleteCascading(user.getId(), con);
		}


		for (User user : usersToRemove) {
			Map<String, User> userNameMap = schoolIdToUserNameMap.get(user.getSchool_id());

			if(userNameMap != null){
				userNameMap.remove(user.getUsername());
			}

			userKeys.remove(user.getId());

		}

    }

    public Permission findPermissionByName(String permission_name){
		return permissionNameMap.get(permission_name);
	}


	public void addPermissionToRole(Role role, Permission permission, Connection con) {

    	role.addPermission(permission);
    	RoleDAO.update(role, con);
    	updateUserPermissions(role);

    }

	public void removePermissionFromRole(Role role, Permission permission, Connection con) {

    	role.removePermission(permission);
    	RoleDAO.update(role, con);
		updateUserPermissions(role);

    }

	/**
	 * After given role has changed update permissions of all users with given role
	 * @param role
	 */
	private void updateUserPermissions(Role role){
		// only users of scholl to which role belongs are affected:
		Map<String, User> nameUserMap = schoolIdToUserNameMap.get(role.getSchool_id());

		nameUserMap.forEach((name, user) -> {
			user.registerAllPermissions();
		});

	}


	public Role getRoleBySchoolIdAndName(Long school_id, String name) {

		List<Role> roleList = schoolIdToRoleListMap.get(school_id);
		if(roleList != null){
			for (Role role : roleList) {
				if(role.getName().equals(name)){
					return role;
				}
			}
		}

		return null;
	}

	public void removeRoles(List<Role> rolesToRemove, Connection con) {

		/*
		 * If something goes wrong in database operations
		 * we don't want to update object model. Therefore
		 * we do two separate iterations over List rolesToRemove.
		 */

		for (Role role : rolesToRemove) {
			RoleDAO.delete(role, con);
		}

		for (Role role : rolesToRemove) {

			List<Role> roleList = schoolIdToRoleListMap.get(role.getSchool_id());
			if(roleList != null){
				roleList.remove(role);
			}
			roleKeys.remove(role.getId());
		}

	}

	public List<Role> getRoleListBySchoolId(Long school_id) {

		List<Role> roleList = schoolIdToRoleListMap.get(school_id);

		if(roleList != null){
			return roleList;
		}

		return new ArrayList<>();
	}

	public List<User> getUserListBySchoolId(Long school_id) {

		Map<String, User> userNameMap = schoolIdToUserNameMap.get(school_id);

		List<User> userList = new ArrayList<>();

		if(userNameMap != null){
			userNameMap.forEach((name, user) -> userList.add(user));
		}

		return userList;

	}
}

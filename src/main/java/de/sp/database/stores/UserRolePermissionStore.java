package de.sp.database.stores;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.sql2o.Connection;

import de.sp.database.connection.ConnectionPool;
import de.sp.database.daos.basic.RoleDAO;
import de.sp.database.daos.basic.UserDAO;
import de.sp.database.daos.basic.UserRoleDAO;
import de.sp.database.model.Role;
import de.sp.database.model.School;
import de.sp.database.model.User;
import de.sp.main.resources.modules.Permission;

/**
 * 
 * Stores Schools, Users, Roles and Permissions
 * 
 * @author M. Pabst, 2016
 *
 */

public class UserRolePermissionStore {

	private static UserRolePermissionStore instance;

	private List<User> users = new ArrayList<>();
	private Map<Long, User> userKeys = new HashMap<>(); // maps User.id to User
	private Map<Long, Map<String, User>> schoolUserNameMap = new HashMap<>();

	private List<Role> roles = new ArrayList<>();
	private Map<Long, Role> roleKeys = new HashMap<>(); // maps Role.id to role

	private List<Permission> permissions = new ArrayList<>();
	private Map<String, Permission> permissionNameMap = new HashMap<>();

	public static UserRolePermissionStore getInstance() {

		if (instance == null) {
			instance = new UserRolePermissionStore();
		}

		return instance;

	}

	public void loadFromDatabase() {

		try (Connection con = ConnectionPool.open()) {

			UserDAO.getAll(con).forEach(user -> addUser(user));

			RoleDAO.getAll(con).forEach(role -> addRole(role));

			roles.forEach(role -> role.registerPermissions(permissionNameMap));

			Map<Long, School> schoolKeys = SchoolTermStore.getInstance()
					.getSchoolKeys();

			UserRoleDAO.joinUsersWithRoles(userKeys, roleKeys, schoolKeys, con);
		}

	}

	public void addRole(Role role) {
		roles.add(role);
		roleKeys.put(role.getId(), role);
		role.registerPermissions(permissionNameMap);

		Map<Long, School> schoolKeys = SchoolTermStore.getInstance()
				.getSchoolKeys();

		role.setSchool(schoolKeys.get(role.getSchool_id()));
	}

	public void addUser(User user) {

		users.add(user);
		userKeys.put(user.getId(), user);

		Map<String, User> userNameMap = schoolUserNameMap.get(user.getSchool_id());

		if(userNameMap == null){
			userNameMap = new HashMap<>();
			schoolUserNameMap.put(user.getSchool_id(), userNameMap);
		}

		userNameMap.put(user.getUsername(), user);

	}

	public void addPermission(Permission permission) {
		if (permissionNameMap.get(permission.getName()) == null) {
			permissions.add(permission);
			permissionNameMap.put(permission.getName(), permission);
		}
	}

	public void addPermissions(List<Permission> permissionsList) {

		permissionsList.forEach(permission -> addPermission(permission));

	}

	public User getUserBySchoolIdAndName(Long school_id, String username) {

		Map<String, User> userNameMap = schoolUserNameMap.get(school_id);

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
}

package de.sp.database.daos.basic;

import de.sp.database.model.Role;
import de.sp.database.model.School;
import de.sp.database.model.User;
import de.sp.database.model.UserRole;
import de.sp.database.statements.StatementStore;
import org.sql2o.Connection;

import java.util.List;
import java.util.Map;

public class UserRoleDAO {

	public static List<UserRole> getAll(Connection con) {

		String sql = StatementStore.getStatement("user_role.getAll");

		List<UserRole> userRoleList = con.createQuery(sql).executeAndFetch(
				UserRole.class);

		return userRoleList;

	}

	public static void joinUsersWithRoles(Map<Long, User> users,
			Map<Long, Role> roles, Map<Long, School> schools, Connection con) {

		List<UserRole> userRoles = getAll(con);

		for (UserRole ur : userRoles) {

			Role role = roles.get(ur.role_id);
			User user = users.get(ur.user_id);

			if (role != null && user != null) {

				user.addRole(role);

			}

		}

	}

	/**
	 * 
	 * insert into role (name, remark, school_id) values (:name, :remark,
	 * :school_id)
	 * 
	 * @return
	 * @throws Exception
	 */
	public static UserRole insert(Long user_id, Long role_id, Connection con)
			throws Exception {

		String sql = StatementStore.getStatement("user_role.insert");

		Long id = con.createQuery(sql, true).addParameter("user_id", user_id)
				.addParameter("role_id", role_id).executeUpdate()
				.getKey(Long.class);

		return new UserRole(id, user_id, role_id);

	}

    public static void addRoleToUser(Long user_id, Long role_id, Connection con) {

		String sql = StatementStore.getStatement("user_role.addRoleForUser");

		con.createQuery(sql)
				.addParameter("user_id", user_id)
				.addParameter("role_id", role_id)
				.executeUpdate();

	}

    public static void removeRoleFromUser(Long user_id, Long role_id, Connection con) {

		String sql = StatementStore.getStatement("user_role.removeRoleFromUser");

		con.createQuery(sql)
				.addParameter("user_id", user_id)
				.addParameter("role_id", role_id)
				.executeUpdate();

	}
}

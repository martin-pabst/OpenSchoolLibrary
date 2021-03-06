package de.sp.database.daos.basic;

import de.sp.database.model.Role;
import de.sp.database.statements.StatementStore;
import org.sql2o.Connection;

import java.util.List;

public class RoleDAO {

	public static List<Role> getAll(Connection con) {

		String sql = StatementStore.getStatement("role.getAll");

		List<Role> rolelist = con.createQuery(sql).executeAndFetch(Role.class);

		return rolelist;

	}

	/**
	 * 
	 * insert into role (name, remark, school_id) values (:name, :remark,
	 * :school_id)
	 * 
	 * @return
	 * @throws Exception
	 */
	public static Role insert(String name, String remark, Long school_id,
			String permissions, Connection con) throws Exception {

		String sql = StatementStore.getStatement("role.insert");

		Long id = con.createQuery(sql, true).addParameter("name", name)
				.addParameter("remark", remark)
				.addParameter("school_id", school_id)
				.addParameter("permissions", permissions).executeUpdate()
				.getKey(Long.class);

		return new Role(id, name, remark, school_id, permissions);

	}

	public static void delete(Role role, Connection con) {
		String sql = StatementStore.getStatement("role.remove");

		con.createQuery(sql).addParameter("id", role.getId()).executeUpdate();

	}

	public static Role getRoleById(Long role_id, Connection con) {

		String sql = "select * from role where id = :role_id";

		return con.createQuery(sql)
				.addParameter("role_id", role_id)
				.executeAndFetchFirst(Role.class);

	}

	public static void update(Role role, Connection con) {

		String sql = StatementStore.getStatement("role.update");

		con.createQuery(sql)
				.addParameter("name", role.getName())
				.addParameter("remark", role.getRemark())
				.addParameter("permissions", role.getPermissions())
				.addParameter("school_id", role.getSchool_id())
				.addParameter("role_id", role.getId())
				.executeUpdate();

	}
}

package de.sp.database.daos.basic;

import java.util.List;

import org.sql2o.Connection;

import de.sp.database.model.User;
import de.sp.database.statements.StatementStore;
import de.sp.tools.string.PasswordSecurity;
import de.sp.tools.string.SaltAndHash;

public class UserDAO {

	public static User getUserByUsername(String username, Connection con) {

		String sql = StatementStore.getStatement("users.getUserByName");

		List<User> userlist = con.createQuery(sql)
				.addParameter("username", username).executeAndFetch(User.class);

		if (userlist.size() == 1) {
			return userlist.get(0);
		}

		return null;

	}

	public static User insert(String username, String name, String password,
			String languageCode, Long last_selected_school_term_id, Boolean is_admin,
			Connection con) throws Exception {

		String sql = StatementStore.getStatement("users.insert");

		SaltAndHash saltAndHash = PasswordSecurity.getSaltedHash(password);

		Long id = con
				.createQuery(sql, true)
				.addParameter("username", username)
				.addParameter("salt", saltAndHash.getSalt())
				.addParameter("hash", saltAndHash.getHash())
				.addParameter("name", name)
				.addParameter("languageCode", languageCode)
				.addParameter("last_selected_school_term_id",
						last_selected_school_term_id)
				.addParameter("is_admin", is_admin)
				.executeUpdate()
				.getKey(Long.class);

		return new User(id, username, name, saltAndHash.getHash(),
				saltAndHash.getSalt(), languageCode, is_admin);

	}

	public static void delete(Long id, Connection con) {

		String sql = StatementStore.getStatement("users.remove");

		con.createQuery(sql).addParameter("id", id).executeUpdate();

	}

	public static List<User> getAll(Connection con) {

		String sql = StatementStore.getStatement("users.getAll");

		List<User> userlist = con.createQuery(sql).executeAndFetch(User.class);

		return userlist;

	}

	public static void update(User user, Connection con) throws Exception {

		String sql = StatementStore.getStatement("users.update");

		con.createQuery(sql)
				.addParameter("id", user.getId())
				.addParameter("username", user.getUsername())
				.addParameter("name", user.getName())
				.addParameter("languageCode", user.getLanguageCode())
				.addParameter("last_selected_school_term_id",
						user.getLast_selected_school_term_id())
				.addParameter("is_admin", user.is_admin())
				.executeUpdate();

	}

}

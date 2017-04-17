package de.sp.database.daos.basic;

import java.util.List;

import org.sql2o.Connection;

import de.sp.database.model.Value;
import de.sp.database.statements.StatementStore;

public class ValueDAO {
	public static List<Value> getAll(Connection con) {

		String sql = StatementStore.getStatement("value.getAll");
		List<Value> valuelist = con.createQuery(sql).executeAndFetch(
				Value.class);
		return valuelist;

	}

	public static Value insert(Long valuestore_key, Long school_id,
			String name, String abbreviation, String external_key,
			Connection con) throws Exception {

		String sql = StatementStore.getStatement("value.insert");

		Long id = con.createQuery(sql, true)
				.addParameter("valuestore_key", valuestore_key)
				.addParameter("school_id", school_id)
				.addParameter("name", name)
				.addParameter("abbreviation", abbreviation)
				.addParameter("external_key", external_key).executeUpdate()
				.getKey(Long.class);

		return new Value(id, valuestore_key, school_id, name, abbreviation,
				external_key);

	}

	public static void delete(Value value, Connection con) {

		String sql = StatementStore.getStatement("value.delete");

		con.createQuery(sql)

		.addParameter("id", value.getId())

		.executeUpdate();

	}

	public static List<Value> findBySchoolAndValueStore(Long school_id,
			Long valuestore_key, Connection con) {

		String sql = StatementStore
				.getStatement("value.findBySchoolAndValueStore");
		List<Value> valuelist = con.createQuery(sql)
				.addParameter("school_id", school_id)
				.addParameter("valuestore_key", valuestore_key)
				.executeAndFetch(Value.class);
		return valuelist;

	}

	public static Value findBySchoolAndValueStoreAndExternalKey(Long school_id,
			Long valuestore_key, String external_key, Connection con) {

		String sql = StatementStore
				.getStatement("value.findBySchoolAndValueStoreAndExternalKey");
		List<Value> valuelist = con.createQuery(sql)
				.addParameter("school_id", school_id)
				.addParameter("valuestore_key", valuestore_key)
				.addParameter("external_key", external_key)
				.executeAndFetch(Value.class);

		if (valuelist != null && valuelist.size() > 0) {
			return valuelist.get(0);
		}

		return null;

	}

	public static Value findOrWrite(Long school_id, Long valuestore_key,
			String external_key, Connection con, String name,
			String abbreviation) throws Exception {

		Value value = findBySchoolAndValueStoreAndExternalKey(school_id,
				valuestore_key, external_key, con);
		
		if(value != null){
			return value;
		}
		
		return insert(valuestore_key, school_id, name, abbreviation, external_key, con);

	}

}

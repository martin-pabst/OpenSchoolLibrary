package de.sp.database.daos.basic;

import java.util.List;

import org.sql2o.Connection;

import de.sp.database.model.Address;
import de.sp.database.statements.StatementStore;

public class AddressDAO {
	public static List<Address> getAll(Connection con) {

		String sql = StatementStore.getStatement("address.getAll");
		List<Address> addresslist = con.createQuery(sql).executeAndFetch(
				Address.class);
		return addresslist;

	}

	public static Address insert(String leading_address_text,
			String street_name, String house_number, String postal_code,
			String city, String country, String post_office_box,
			String box_postal_code, String salutation_text, Connection con) throws Exception {

		String sql = StatementStore.getStatement("address.insert");

		Long id = con.createQuery(sql, true)
				.addParameter("leading_address_text", leading_address_text)
				.addParameter("street_name", street_name)
				.addParameter("house_number", house_number)
				.addParameter("postal_code", postal_code)
				.addParameter("city", city).addParameter("country", country)
				.addParameter("post_office_box", post_office_box)
				.addParameter("box_postal_code", box_postal_code)
				.addParameter("salutation_text", salutation_text)
				.executeUpdate().getKey(Long.class);

		return new Address(id, leading_address_text, street_name, house_number,
				postal_code, city, country, post_office_box, box_postal_code, salutation_text);

	}

	public static void update(Address address, Connection con) throws Exception {

		String sql = StatementStore.getStatement("address.update");

		con.createQuery(sql)
				.addParameter("id", address.getId())
				.addParameter("leading_address_text",
						address.getLeading_address_text())
				.addParameter("street_name", address.getStreet_name())
				.addParameter("house_number", address.getHouse_number())
				.addParameter("postal_code", address.getPostal_code())
				.addParameter("city", address.getCity())
				.addParameter("country", address.getCountry())
				.addParameter("post_office_box", address.getPost_office_box())
				.addParameter("box_postal_code", address.getBox_postal_code())
				.addParameter("salutation_text", address.getSalutation_text())
				.executeUpdate();
	}

	public static void delete(Long id, Connection con) {

		if (id == null) {
			return;
		}

		String sql = StatementStore.getStatement("address.delete");

		con.createQuery(sql)

		.addParameter("id", id)

		.executeUpdate();

	}

	public static Address findById(Long id, Connection con) {

		String sql = StatementStore.getStatement("address.findById");
		Address address = con.createQuery(sql).executeAndFetchFirst(
				Address.class);
		return address;

	}

}

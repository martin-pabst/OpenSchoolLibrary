package de.sp.database.model;


public class Address {

	private Long id;

	private String leading_address_text;

	private String street_name;

	private String house_number;

	private String postal_code;

	private String city;

	private String country;

	private String post_office_box;

	private String box_postal_code;

	private String salutation_text;

	public Address() {
	}

	public Address(Long id, String leading_address_text, String street_name,
			String house_number, String postal_code, String city,
			String country, String post_office_box, String box_postal_code,
			String salutation_text) {

		this.id = id;
		this.leading_address_text = leading_address_text;
		this.street_name = street_name;
		this.house_number = house_number;
		this.postal_code = postal_code;
		this.city = city;
		this.country = country;
		this.post_office_box = post_office_box;
		this.box_postal_code = box_postal_code;
		this.salutation_text = salutation_text;
	}

	public Long getId() {

		return id;

	}

	public String getLeading_address_text() {

		return leading_address_text;

	}

	public String getStreet_name() {

		return street_name;

	}

	public String getHouse_number() {

		return house_number;

	}

	public String getPostal_code() {

		return postal_code;

	}

	public String getCity() {

		return city;

	}

	public String getCountry() {

		return country;

	}

	public String getPost_office_box() {

		return post_office_box;

	}

	public String getBox_postal_code() {

		return box_postal_code;

	}

	public String getSalutation_text() {
		return salutation_text;
	}

}
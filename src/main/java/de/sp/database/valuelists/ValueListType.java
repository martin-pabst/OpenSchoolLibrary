package de.sp.database.valuelists;

public enum ValueListType {
	
	curriculum("Bildungsgang", 1l), 
	form("form", 2l), // de: "Jahrgangsstufe"
	contact_type("contact type", 3l),
	grade("Grade", 4l); // academic grades
	
	private String name;
	private Long key;
	
	private ValueListType(String name, Long key) {
		this.name = name;
		this.key = key;
	}

	public Long getKey() {

		return key;
	
	}

	public String getName() {
		return name;
	}
	
	
	
	
}

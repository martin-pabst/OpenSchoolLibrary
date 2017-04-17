package de.sp.asvsst.model.wertelisten;

import de.sp.asvsst.ParseASVDataException;

public enum ASVKommunikationsTyp {

	telefon("01", "Telefon", "Tel."), mobiltelefon("02", "Mobiltelefon",
			"Mobil"), fax("03", "Telefax", "Fax"), mail("04", "E-Mail", "Mail"), url(
			"05", "URL", "URL");

	private String schluessel;
	private String name;
	private String abbreviation;

	private ASVKommunikationsTyp(String schluessel, String name,
			String abbreviation) {
		this.schluessel = schluessel;
		this.name = name;
		this.abbreviation = abbreviation;
	}

	public static ASVKommunikationsTyp findBySchluessel(String schluessel)
			throws ParseASVDataException {

		for (ASVKommunikationsTyp typ : ASVKommunikationsTyp.values()) {

			if (typ.schluessel.equals(schluessel)) {
				return typ;
			}

		}

		throw new ParseASVDataException("Der Kommunikationstyp mit Schlüssel "
				+ schluessel == null ? "null" : schluessel
				+ " ist nicht bekannt.");

	}

	public String getSchluessel() {
		return schluessel;
	}

	public String getName() {
		return name;
	}

	public String getAbbreviation() {
		return abbreviation;
	}

}

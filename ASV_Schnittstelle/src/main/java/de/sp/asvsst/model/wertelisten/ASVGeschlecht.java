package de.sp.asvsst.model.wertelisten;

import de.sp.asvsst.ParseASVDataException;

public enum ASVGeschlecht {
	M("1", "M", "Männlich", "Männlich"), //
	W("2", "W", "Weiblich", "Weiblich"); //

	private String schluessel;
	private String kurzform;
	private String anzeigeform;
	private String langform;

	private ASVGeschlecht(String schluessel, String kurzform,
			String anzeigeform, String langform) {
		this.schluessel = schluessel;
		this.kurzform = kurzform;
		this.anzeigeform = anzeigeform;
		this.langform = langform;
	}

	public static ASVGeschlecht findBySchluessel(String schluessel)
			throws ParseASVDataException {

		if (schluessel == null) {
			return null;
		}

		for (ASVGeschlecht geschlecht : ASVGeschlecht.values()) {
			if (geschlecht.schluessel.equals(schluessel)) {
				return geschlecht;
			}
		}

		throw new ParseASVDataException("Das Geschlecht mit Schlüssel "
				+ schluessel + " ist nicht bekannt.");

	}

	public String getSchluessel() {
		return schluessel;
	}

	public String getKurzform() {
		return kurzform;
	}

	public String getAnzeigeform() {
		return anzeigeform;
	}

	public String getLangform() {
		return langform;
	}
	
	

}
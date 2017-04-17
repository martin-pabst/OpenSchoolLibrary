package de.sp.asvsst.model.wertelisten;

import de.sp.asvsst.ParseASVDataException;

public enum ASVKlassenleitungArt {
	
	Kl_Leitung("K", "Kl-Leitung", "Klassenleitung", "Klassenleitung"), // 
	St_Leitung("S", "St-Leitung", "Stellvertr. Klassenleitung", "Stellvertretende Klassenleitung"); // 

	private String schluessel;
	private String kurzform;
	private String anzeigeform;
	private String langform;
	
	private ASVKlassenleitungArt(String schluessel, String kurzform,
			String anzeigeform, String langform) {
		this.schluessel = schluessel;
		this.kurzform = kurzform;
		this.anzeigeform = anzeigeform;
		this.langform = langform;
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

	public static ASVKlassenleitungArt findBySchluessel(String schluessel)
			throws ParseASVDataException {

		if (schluessel == null) {
			return null;
		}

		for (ASVKlassenleitungArt KlassenleitungArt : ASVKlassenleitungArt.values()) {
			if (KlassenleitungArt.schluessel.equals(schluessel)) {
				return KlassenleitungArt;
			}
		}

		throw new ParseASVDataException(
				"Der KlassenleitungArt mit Schlüssel " + schluessel + " ist nicht bekannt.");

	}

	

}

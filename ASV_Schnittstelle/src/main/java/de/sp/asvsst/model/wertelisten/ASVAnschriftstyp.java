package de.sp.asvsst.model.wertelisten;

import de.sp.asvsst.ParseASVDataException;

public enum ASVAnschriftstyp {
	GH("30", "GH", "Hauptanschrift Gebäude", "Hauptanschrift - Datenbereich: Gebäude"), // 
	GW("31", "GW", "weitere Anschrift Gebäude", "weitere Anschrift - Datenbereich: Gebäude"), // 
	GA("10", "GA", "gewöhnlicher Aufenthalt", "Wie bei gewöhnlicher Aufenthalt Datenbereich: Schüler"), // 
	VA("11", "VA", "Vor Antritt der Ausbildung", "Vor Antritt der Ausbildung Datenbereich: Schüler"), // 
	EW("08", "EW", "Eine Wohnung", "Eine Wohnung Datenbereich: Schüler"), // 
	EH("09", "EH", "Ein Wohnheim", "Ein Wohnheim Datenbereich: Schüler"), // 
	GE("04", "GE", "gem. Erziehungsberechtigten", "beim Erziehungsberechtigten bzw. bei den gemeinsamen Erziehungsberechtigten Datenbereich: Schüler"), // 
	WE("05", "WE", "weit. Erziehungsberechtigten", "beim weiteren Erziehungsberechtigten Datenbereich: Schüler"), // 
	SO("07", "SO", "in einer sonstigen Wohnung", "in einer sonstigen Wohnung, ggf. Unterbringung bei (Oma, Gastfamilie, ..) Datenbereich: Schüler"), // 
	EA("03", "EA", "Eigene Anschrift", "Eigene Anschrift Datenbereich: Schüler"), // 
	WZ("02", "WZ", "Wie bei Erziehungsberechtigter", "Wie bei Erziehungsberechtigter Datenbereich: Schüler"), // 
	WO("06", "WO", "in einem Wohnheim", "in einem Wohnheim Datenbereich: Schüler"), // 
	EZ("01", "EZ", "Erziehungsberechtigter", "Erziehungsberechtigter Datenbereich: Schüler"), // 
	DH("20", "DH", "Hauptanschrift Schule", "Hauptanschrift - Datenbereich: Schule"), // 
	DW("21", "DW", "weitere Anschrift Schule", "weitere Anschrift - Datenbereich: Schule"); // 

	private String schluessel;
	private String kurzform;
	private String anzeigeform;
	private String langform;
	
	private ASVAnschriftstyp(String schluessel, String kurzform,
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

	public static ASVAnschriftstyp findBySchluessel(String schluessel)
			throws ParseASVDataException {

		if (schluessel == null) {
			return null;
		}

		for (ASVAnschriftstyp staat : ASVAnschriftstyp.values()) {
			if (staat.schluessel.equals(schluessel)) {
				return staat;
			}
		}

		throw new ParseASVDataException(
				"Der Anschriftstyp mit Schlüssel " + schluessel + " ist nicht bekannt.");

	}

	
	}
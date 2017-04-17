package de.sp.database.testdata.valuelists;

import de.sp.database.valuelists.ValueNotFoundException;

public enum ASVJahrgangsstufe {
	jgst3("031", "3", "Jgst 3", 3), //
	jgst4("041", "4", "Jgst 4", 4), //
	jgst5("051", "5", "Jgst 5", 5), //
	jgst6("061", "6", "Jgst 6", 6), //
	jgst7("071", "7", "Jgst 7", 7), //
	jgst8("081", "8", "Jgst 8", 8), //
	jgst8p1("083", "8.1", "Teiljgst 8.1", 8), //
	jgst8p2("084", "8.2", "Teiljgst 8.2", 8), //
	jgst8A1("085", "8A1", "Jgst 8 A1", 8), //
	jgst8A2("086", "8A2", "Jgst 8 A2", 8), //
	jgst9("091", "9", "Jgst 9", 9), //
	jgst9p1("093", "9.1", "Teiljgst 9.1", 9), //
	jgst9p2("094", "9.2", "Teiljgst 9.2", 9), //
	jgst9plus("095", "9+", "Jgst 9+", 9), //
	jgst10("101", "10", "Jgst 10", 10), //
	jgst11("111", "11", "Jgst 11", 11), //
	jgst12("121", "12", "Jgst 12", 12), //
	jgst13("131", "13", "Jgst 13", 13), //
	jgstVKU("994", "VKU", "Vorkurs", 10); //

	private String schluessel;
	private String kurzform;
	private String anzeigeform;
	private int schulbesuchsjahr;
	
	private ASVJahrgangsstufe(String schluessel, String kurzform,
			String anzeigeform, int schulbesuchsjahr) {
		this.schluessel = schluessel;
		this.kurzform = kurzform;
		this.anzeigeform = anzeigeform;
		this.schulbesuchsjahr = schulbesuchsjahr;
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

	public int getSchulbesuchsjahr() {
		return schulbesuchsjahr;
	}

	public static ASVJahrgangsstufe findBySchluessel(String schluessel)
			throws ValueNotFoundException {

		if (schluessel == null) {
			return null;
		}

		for (ASVJahrgangsstufe staat : ASVJahrgangsstufe.values()) {
			if (staat.schluessel.equals(schluessel)) {
				return staat;
			}
		}

		throw new ValueNotFoundException(
				"Die Jahrgangsstufe mit Schlüssel " + schluessel + " ist nicht bekannt.");

	}

	
	}
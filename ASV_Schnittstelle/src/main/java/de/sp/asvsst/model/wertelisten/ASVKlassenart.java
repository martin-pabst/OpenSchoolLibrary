package de.sp.asvsst.model.wertelisten;

import de.sp.asvsst.ParseASVDataException;

public enum ASVKlassenart {
	S("53", "S", "Sonderkl. Aussiedler", "Sonderklasse für Aussiedler und Aussiedlerabiturienten"), // 
	KA9PLUS2("28", "9+2", "Klasse im 9+2 Angebot", "Klasse im 9+2 Angebot"), // 
	GYA("65", "GYA", "Abiturabschlussklasse", "Abiturabschlussklasse"), // 
	MS("61", "MS", "Mittelschulzug", "Mittelschulzug"), // 
	KR("25", "KR", "Klasse für Kranke", "Klasse für Kranke"), // 
	SVE("26", "SVE", "schulvorb. Einrichtung", "schulvorbereitende Einrichtung"), // 
	KOPSCH("35", "KOPSCH", "Kooperationsschüler", "für Kooperationsschüler eingerichtete Klasse (aber nicht Kooperationsklasse nach nach Art. 30 Abs. 1 Satz 4 ff. BayEUG)"), // 
	EX_QA("81", "EX-QA", "ext. Prüfungsteilnehmer QA", "Externe Prüfungsteilnehmer am Quali"), // 
	KURS("85", "KURS", "Kursphase der Oberstufe", "Kursphase der Oberstufe"), // 
	ORG("84", "ORG", "Organisationsklasse", "Organisationsklasse"), // 
	EX_MA("82", "EX-MA", "ext. Prüfungsteilnehmer MA", "Externe Prüfungsteilnehmer am Mittleren Bildungsabschluss"), // 
	AUSL("83", "AUSL", "Auslandsaufenthalte", "für Auslandsaufenthalt beurlaubte Schüler"), // 
	EX("80", "EX", "externe Prüfungsteilnehmer", "Externe Prüfungsteilnehmer (andere Bewerber)"), // 
	UE("42", "UE", "Übergangsklasse f. Ausländer", "Übergangsklasse für Schüler ausländischer Herkunft"), // 
	BVB("23", "BVB", "BVB-Maßnahme", "BVB-Maßnahme schulischer Teil"), // 
	GY("63", "GY", "Gymnasialzug", "Gymnasialzug"), // 
	RS("62", "RS", "Realschulzug", "Realschulzug"), // 
	RSA("64", "RSA", "Realschulabschlussklasse", "Realschulabschlussklasse"), // 
	BGJ("06", "BGJ", "Berufsgrundschuljahr (BJG/s)", "Berufsgrundschuljahr"), // 
	R("00", "R", "Regelklasse", "Regelklasse (normale Klasse)"), // 
	BGJ_k("01", "BGJ/k", "BGJ-kooperativ (BGJ/k)", "Berufsgrundbildungsjahr/kooperativ"), // 
	BVJ("07", "BVJ", "Berufsvorbereitungsjahr", "Berufsvorbereitungsjahr gem. § 28 Abs. 5 BSO bzw. § 12 Abs. 5 BSO-B"), // 
	EK("71", "EK", "Einführungsklasse", "Einführungsklasse (nicht: Profiklassen an Landschulheimen)"), // 
	BK("27", "BK", "Berufsorientierungsklasse", "Berufsorientierungsklasse (B-Klasse)"), // 
	BEJ("08", "BEJ", "BVJ Berufseinstiegsjahr", "Klasse der Berufsvorbereitung oder des Berufseinstiegs, die in Kooperation mit außerschulischen Partnern unterrichtet wird"), // 
	JG("05", "JG", "Jungarbeiterklasse", "Jungarbeiterklasse gemäß § 28 Abs. 4 BSO bzw. § 12 Abs. 7 BSO-B"), // 
	V("73", "V", "Vorkurs", "Vorkurs (FOS, BOS, KOL und Spätberufenen-GY)"), // 
	PK("16", "PK", "Praxisklasse", "Praxisklasse"), // 
	BHD("09", "BHD", "Hochschule Dual", "Klasse im Bildungsgang Hochschule Dual"), // 
	DAH("10", "DAH", "Ausbildung + FH-Reife (DBFH)", "Klasse des Bildungsgangs \"Duale Berufsausbildung und Fachhochschulreife\""), // 
	WG("20", "WG", "BS-Stufe (geistige Entwickl.)", "Berufsschulstufe für Förderschwerpunkt \"geistige Entwicklung\""), // 
	FK3("03", "FK3", "Fkl. § 28 Abs.3 BSO", "Fachklasse gemäß § 28 Abs. 3 BSO"), // 
	DIAG("17", "DIAG", "Sonderpäd. Diagnose-/Förd.", "Sonderpädagogische Diagnose- und Förderklasse"), // 
	MK("15", "MK", "M-Klasse (Mittlere Reife Zug)", "M-Klasse (Mittlere Reife Zug)"), // 
	BP("97", "BP", "Berufspraktikanten", "Berufspraktikanten"), // 
	OS("60", "OS", "Orientierungsstufe", "nicht abschlussbezogene Klasse (Orientierungsstufe)"), // 
	FK2("02", "FK2", "Fkl. §28 (2) BSO/§12 (2) BSO-B", "Fachklasse gemäß § 28 Abs. 2 BSO bzw. § 12 Abs. 2 BSO-B (ohne BGJ/k)"), // 
	FK6("04", "FK6", "Kl. Verwandt. Ausbildungsber.", "Klasse verwandter Ausbildungsberufe gemäß § 28 Abs. 6 BSO bzw. § 12 Abs. 4 BSO-B (ohne BGJ/k)"), // 
	BVJ_B("21", "BVJ-B", "BVJ", "Berufsvorbereitungsjahr"), // 
	EVP("98", "EVP", "Erzieher-/Vorpraktikanten", "Erzieher-/Vorpraktikanten"); // 

	private String schluessel;
	private String kurzform;
	private String anzeigeform;
	private String langform;
	
	private ASVKlassenart(String schluessel, String kurzform,
			String anzeigeform, String langform) {
		this.schluessel = schluessel;
		this.kurzform = kurzform;
		this.anzeigeform = anzeigeform;
		this.langform = langform;
	}
	
	public static ASVKlassenart findBySchluessel(String schluessel)
			throws ParseASVDataException {

		if (schluessel == null) {
			return null;
		}

		for (ASVKlassenart Klassenart : ASVKlassenart.values()) {
			if (Klassenart.schluessel.equals(schluessel)) {
				return Klassenart;
			}
		}

		throw new ParseASVDataException(
				"Der Klassenart mit Schlüssel " + schluessel + " ist nicht bekannt.");

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
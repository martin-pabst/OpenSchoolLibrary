package de.sp.database.testdata.valuelists;

import de.sp.database.valuelists.ValueNotFoundException;


public enum ASVBildungsgang {

	GY_NTG_8("0603008009", "GY_NTG_8", "GY Naturwissenschaftl.-techn.", "GY Naturwissenschaftlich-technologisch-G8"), // 
	GY_SG_8("0603007007", "GY_SG_8", "GY Sprachl. - nicht humanist.", "GY Sprachlich-G8"), // 
	GY("0603001001", "GY", "GY ohne Ausbildungsrichtung", "GY ohne Ausbildungsrichtung"), // 
	GY_HG_8("0603005002", "GY_HG_8", "GY Sprachl. - humanist.", "GY Humanistisch-G8"), // 
	GY_WSG_S_8("0603009011", "GY_WSG-S_8", "GY Wirt./Sozialw. - Sozialw.", "GY Wirtschafts- und Sozialwissenschaftlich (S)-G8"), // 
	GY_WSG_W_8("0603010013", "GY_WSG-W_8", "GY Wirt./Sozialw. - Wirtsch.", "GY Wirtschafts- und Sozialwissenschaftlich (W)-G8"), // 
	GY_MuG_8("0603011017", "GY_MuG_8", "GY Musisch-G8", "GY Musisch-G8"), // 
//	RSF_es("0512005019", "RSF_es", "RSF ohne WPFG e-s-Entw.", "RSF ohne Wahlpflichtfaechergruppe-Zuordnung emotionale und soziale Entwicklung"), // 
//	RSF_I_es("0512005020", "RSF_I_es", "RSF WPFG I e-s-Entwicklung", "RSF Wahlpflichtfaechergruppe I emotionale und soziale Entwicklung"), // 
//	RSF_IIIb_e("0512005023", "RSF_IIIb_e", "RSF WPFG IIIb e-s-Entwicklung", "RSF Wahlpflichtfaechergruppe IIIb emotionale und soziale Entwicklung"), // 
//	RSF_IIIa_S("0512002004", "RSF IIIa_S", "RSF WPFG IIIa Sehen", "RSF Wahlpflichtfaechergruppe IIIa Sehen"), // 
	GYF("0613001003", "GYF", "GYF ohne Ausbildungsrichtung", "GYF ohne Ausbildungsrichtung"), // 
	GY_Spaet_OS("0603006009", "GY_Spaet OS", "GY Spaetberufenen OST", "GY Spaetberufenengymnasium Oberstufe"), // 
	GYF_8("0613008001", "GYF_8", "GYF Naturw.-tech.-G8 e-s-Entw.", "GYF Naturwissenschaftlich-technologisch-G8 emotional-soziale-Entwicklung"), // 
//	RS_IIIb9Plus2("0503004009", "RS_IIIb9Plus2", "RS WPFG IIIb 9Plus2", "RS Wahlpflichtfaechergruppe IIIb im 9Plus2-Modell"), // 
//	RS_IIIa9Plus2("0503004008", "RS_IIIa9Plus2", "RS WPFG IIIa 9Plus2", "RS Wahlpflichtfaechergruppe IIIa im 9Plus2-Modell"), // 
//	RS_II9Plus2("0503003007", "RS_II9Plus2", "RS WPFG II 9Plus2", "RS Wahlpflichtfaechergruppe II im 9Plus2-Modell"), // 
//	RS_I9Plus2("0503002006", "RS_I9Plus2", "RS WPFG I 9Plus2", "RS Wahlpflichtfaechergruppe I im 9Plus2-Modell"), // 
	GY_Spaet_VK("0603006006", "GY_Spaet_VK", "GY Spaetberufenen-GY Vorkurs", "GY Spaetberufenengymnasium Vorkurs"), // 
//	RSF_I_Hö("0512003007", "RSF_I_Hö", "RSF WPFG I Hören", "RSF Wahlpflichtfaechergruppe I Hören"), // 
//	RSF_II_Hö("0512003008", "RSF_II_Hö", "RSF WPFG II Hören", "RSF Wahlpflichtfaechergruppe II Hören"), // 
	GY_HG_8MPlus("0603005020", "GY_HG_8MPlus", "GY Sprachl.-humanist. MPlus", "GY Humanistisch-G8 in der Mittelstufe Plus"), // 
	GY_SG_8MPlus("0603007021", "GY_SG_8MPlus", "GY Sprachl.-nicht humanist. MPlus", "GY Sprachlich-G8 in der Mittelstufe Plus"), // 
	GY_NTG_8MPlus("0603008028", "GY_NTG_8MPlus", "GY Naturwissensch.-techn. MPlus", "GY Naturwissenschaftlich-technologisch-G8 in der Mittelstufe Plus"), // 
	GY_WS_S8MPlus("0603009032", "GY_WS-S8MPlus", "GY Wirt./Sozialw.-Sozialw. MPlus", "GY Wirtschafts- und Sozialwissenschaftlich (S)-G8 in der Mittelstufe Plus"), // 
	GY_WS_W8MPlus("0603010033", "GY_WS-W8MPlus", "GY Wirt./Sozialw.-Wirtsch. MPlus", "GY Wirtschafts- und Sozialwissenschaftlich (W)-G8 in der Mittelstufe Plus"), // 
	GY_MuG_8MPlus("0603011034", "GY_MuG_8MPlus", "GY Musisch-G8 MPlus", "GY Musisch-G8 in der Mittelstufe Plus"), // 
	GY_EK("0603012019", "GY_EK", "GY Einführungskl ohne ABR", "GY Einführungsklasse ohne Ausbildungsrichtung"), // 
//	RS_IIIb("0503004005", "RS_IIIb", "RS Wahlpflichtfaechergr. IIIb", "RS Wahlpflichtfaechergruppe IIIb"), // 
//	RS_II("0503003003", "RS_II", "RS Wahlpflichtfaechergruppe II", "RS Wahlpflichtfaechergruppe II"), // 
//	RS_IIIa("0503004004", "RS_IIIa", "RS Wahlpflichtfaechergr. IIIa", "RS Wahlpflichtfaechergruppe IIIa"), // 
//	RS_I("0503002002", "RS_I", "RS Wahlpflichtfaechergruppe I", "RS Wahlpflichtfaechergruppe I"), // 
//	RS("0503001001", "RS", "RS ohne WPFG-Zuordnung", "RS ohne Wahlpflichtfaecher-Zuordnung"), // 
//	RSF_I_km("0512004013", "RSF_I_km", "RSF WPFG I k-m-Entwicklung", "RSF Wahlpflichtfaechergruppe I körperlich-motorische-Entwicklung"), // 
//	RSF_IIIb_S("0512002005", "RSF_IIIb_S", "RSF WPFG IIIb Sehen", "RSF Wahlpflichtfaechergruppe IIIb Sehen"), // 
//	RSF_H("0512003012", "RSF_H", "RSF ohne WPFG Hören", "RSF ohne Wahlpflichtfaechergruppe-Zuordnung Hören"), // 
	GY_Spaet_HG("0603006005", "GY_Spaet_HG", "GY Spaetberufenen-GY Hum", "GY Spaetberufenengymnasium Humanistisch"), // 
//	RSF_II_es("0512005021", "RSF_II_es", "RSF WPFG II e-s-Entwicklung", "RSF Wahlpflichtfaechergruppe II emotionale und soziale Entwicklung"), // 
//	RSF_IIIa_e("0512005022", "RSF_IIIa_e", "RSF WPFG IIIa e-s-Entwicklung", "RSF Wahlpflichtfaechergruppe IIIa emotionale und soziale Entwicklung"), // 
//	RSF_S("0512002006", "RSF_S", "RSF ohne WPFG Sehen", "RSF ohne Wahlpflichtfaechergruppe-Zuordnung Sehen"), // 
//	RSF_I_S("0512002001", "RSF_I_S", "RSF WPFG I Sehen", "RSF Wahlpflichtfaechergruppe I Sehen"), // 
//	RSF_II_S("0512002002", "RSF_II_S", "RSF WPFG II Sehen", "RSF Wahlpflichtfaechergruppe II Sehen"), // 
//	RSF_II_km("0512004014", "RSF_II_km", "RSF WPFG II k-m-Entwicklung", "RSF Wahlpflichtfaechergruppe II körperlich-motorische-Entwicklung"), // 
//	RSF_IIIa_k("0512004016", "RSF IIIa_k", "RSF WPFG IIIa k-m-Entwicklung", "RSF Wahlpflichtfaechergruppe IIIa körperlich-motorische-Entwicklung"), // 
//	RSF_IIIb_k("0512004017", "RSF_IIIb_k", "RSF WPFG IIIb k-m-Entwicklung", "RSF Wahlpflichtfaechergruppe IIIb körperlich-motorische-Entwicklung"), // 
	GYF_Pseu("7165013001", "GYF", "Gymnasium z. sp. F.", "PSEU Gymnasium zur sonderpaedagogischen Förderung"), // 
	GY_Spaet_SG("0603006007", "GY_Spaet SG", "GY Spaetberufenen-GY SG", "GY Spaetberufenengymnasium Sprachlich"), // 
	GY_Ost_G8("0603017031", "GY_Ost_G8", "GY Oberstufe", "GY Oberstufe/Qualifikationsphase G8"), // 
//	RS_Pseu("7135010001", "RS", "Realschule", "PSEU Realschule"), // 
//	RSF("7145011001", "RSF", "Realschule z. sp. F.", "PSEU Realschule zur sonderpaedagogischen Förderung"), // 
	GY_Pseu("7155012001", "GY", "Gymnasium", "PSEU Gymnasium"), // 
	GY_MuG_6("0603011015", "GY_MuG_6", "GY Musisch-G6", "GY Musisch-G6"), // 
//	RSF_IIIa_H("0512003010", "RSF IIIa_H", "RSF WPFG IIIa Hören", "RSF Wahlpflichtfaechergruppe IIIa Hören"), // 
//	RSF_IIIb_H("0512003011", "RSF_IIIb_H", "RSF WPFG IIIb Hören", "RSF Wahlpflichtfaechergruppe IIIb Hören"), // 
//	RSF_k("0512004018", "RSF_k", "RSF ohne WPFG k-m-Entwicklung", "RSF ohne Wahlpflichtfaechergruppe-Zuordnung körperlich-motorische-Entwicklung"), // 
	GY_Spaet_EK("0603006008", "GY_Spaet EK", "GY Spaetb. Einf.kl. ohne ABR", "GY Spaetberufenengymnasium Einführungsklasse ohne Ausbilungsrichtung"); // 
	

	private String schluessel;
	private String kurzform;
	private String anzeigeform;
	private String langform;

	private ASVBildungsgang(String schluessel, String kurzform, String anzeigeform,
			String langform) {
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

	public static ASVBildungsgang findBySchluessel(String schluessel)
			throws ValueNotFoundException {

		if (schluessel == null) {
			return null;
		}

		for (ASVBildungsgang bildungsgang : ASVBildungsgang.values()) {
			if (bildungsgang.schluessel.equals(schluessel)) {
				return bildungsgang;
			}
		}

		throw new ValueNotFoundException(
				"Der Bildungsgang mit Schlüssel " + schluessel + " ist nicht bekannt.");

	}

}
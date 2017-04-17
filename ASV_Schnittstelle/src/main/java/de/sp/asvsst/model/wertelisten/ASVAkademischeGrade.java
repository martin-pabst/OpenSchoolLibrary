package de.sp.asvsst.model.wertelisten;

import de.sp.asvsst.ParseASVDataException;

public enum ASVAkademischeGrade {
	WM("002", "WM", "Werkmeister", "Werkmeister"), //
	OWM("003", "OWM", "Oberwerkmeister", "Oberwerkmeister"), //
	HWM("004", "HWM", "Hauptwerkmeister", "Hauptwerkmeister"), //
	FLA("005", "FLA", "Fachlehreranwärter", "Fachlehreranwärter"), //
	FLzA("006", "FLzA", "Fachlehrer z.A.", "Fachlehrer z.A."), //
	FL("007", "FL", "Fachlehrer", "Fachlehrer"), //
	FoeLA("008", "FöLA", "Förderlehreranwärter", "Förderlehreranwärter"), //
	FoeLzA("009", "FöLzA", "Förderlehrer z.A.", "Förderlehrer z.A."), //
	FoeL("010", "FöL", "Förderlehrer", "Förderlehrer"), //
	FOL("013", "FOL", "Fachoberlehrer", "Fachoberlehrer"), //
	LA("018", "LA", "Lehramtsanwärter", "Lehramtsanwärter"), //
	LzA("020", "LzA", "Lehrer z.A.", "Lehrer z.A."), //
	L("021", "L", "Lehrer", "Lehrer"), //
	_2KR("026", "2.KR", "Zweiter Konrektor", "Zweiter Konrektor"), //
	KR("028", "KR", "Konrektor", "Konrektor"), //
	SoLA("029", "SoLA", "StRef (LA an SoSch)", "Studienreferendar für das Lehramt an Sonderschulen"), //
	SoL("030", "SoL", "Sonderschullehrer", "Sonderschullehrer"), //
	RSLzA("032", "RSLzA", "Realschullehrer z.A.", "Realschullehrer z.A."), //
	RSL("033", "RSL", "Realschullehrer", "Realschullehrer"), //
	SoLzA("036", "SoLzA", "Sonderschullehrer z.A.", "Sonderschullehrer z.A."), //
	BerR("037", "BerR", "Beratungsrektor", "Beratungsrektor"), //
	SBerR("038", "SBerR", "Schulberatungsrektor", "Schulberatungsrektor"), //
	Rbs("039", "R", "Rektor einer besonderen Schule", "Rektor einer besonderen Schule"), //
	SKR("040", "SKR", "Sonderschulkonrektor", "Sonderschulkonrektor"), //
	_2SKR("041", "2.SKR", "Zweiter Sonderschulkonrektor", "Zweiter Sonderschulkonrektor"), //
	SoOL("042", "SoOL", "Sonderschuloberlehrer", "Sonderschuloberlehrer"), //
	ROL("043", "ROL", "Realschuloberlehrer", "Realschuloberlehrer"), //
	R("044", "R", "Rektor", "Rektor"), //
	ALGK("045", "ALGK", "Ausl.Lehrkr.für Gastarb.Kinder", "Ausländ. Lehrkraft für Gastarb.Kinder"), //
	LK_F("046", "LK F", "Lehrkraft f. den franz. Wahlu.", "Lehrkraft f. den franz. Wahlunterricht"), //
	FLSport("047", "FLSport", "Fachlehrer für Sport", "Fachlehrer für Sport"), //
	LK_E("048", "LK E", "Lehrkr.für den engl.Sprachunt.", "Lehrkraft für den engl. Sprachunterricht"), //
	R_FS("049", "R (FS)", "Fachschulrektor", "Fachschulrektor"), //
	BOL("050", "BOL", "Blindenoberlehrer", "Blindenoberlehrer"), //
	StR_GS("051", "StR (GS)", "Studienrat im Grundschuldienst", "Studienrat im Grundschuldienst"), //
	StR_MS("052", "StR (MS)", "Studienrat i.Mittelschuldienst", "Studienrat im Mittelschuldienst"), //
	StR_FoeS("053", "StR (FöS)", "Studienrat im Förderschuldst.", "Studienrat im Förderschuldienst"), //
	StR_RS("054", "StR (RS)", "Studienrat im Realschuldienst", "Studienrat im Realschuldienst"), //
	RSD("055", "RSD", "Realschuldirektor", "Realschuldirektor"), //
	RSK("056", "RSK", "Realschulkonrektor", "Realschulkonrektor"), //
	SR("057", "SR", "Sonderschulrektor", "Sonderschulrektor"), //
	StRef("058", "StRef", "Studienreferendar", "Studienreferendar"), //
	LAss("059", "LAss", "Lehramtsassessor", "Lehramtsassessor"), //
	StRzA("060", "StRzA", "Studienrat z.A.", "Studienrat z.A."), //
	StR("061", "StR", "Studienrat", "Studienrat"), //
	TOL("062", "TOL", "Taubstummenoberlehrer", "Taubstummenoberlehrer"), //
	Laz("064", "L", "Lehrer (AZ)", "Lehrer (AZ)"), //
	VB("065", "VB", "Verwalt-beamter (Reformb. RS)", "Verwaltungsbeamter im Reformbereich (RS)"), //
	D_FS("066", "D(FS)", "Direktor Fachschule", "Direktor einer Fachschule"), //
	Ltd_RSD("068", "Ltd. RSD", "Ltd. Realschuldirektor", "Ltd. Realschuldirektor"), //
	OStR("069", "OStR", "Oberstudienrat", "Oberstudienrat"), //
	StD("071", "StD", "Studiendirektor", "Studiendirektor"), //
	MBR("073", "MBR", "Ltd. Realschulrektor als MB", "Ltd. Realschulrektor als MB"), //
	LOSTD("074", "LOSTD", "Ltd. Oberstudiendirektor", "Ltd. Oberstudiendirektor"), //
	OStD("075", "OStD", "Oberstudiendirektor", "Oberstudiendirektor"), //
	RSRMV("077", "RSRMV", "RSR (als Stellvertr. des MB)", "Realschulrektor (als Stellvertr. des MB)"), //
	SoD("078", "SoD", "Direktor einer Landesschule", "Direktor einer Landesschule"), //
	SD_LSg("079", "SD (LS)", "Dir.Bay.Landesschule Gehörlose", "Direktor Bay. Landesschule Gehörlose"), //
	SD_LSk("080", "SD (LS)", "Dir.Bay. Landesschule Körperb.", "Direktor Bay. Landesschule Körperbeh."), //
	SoKD("081", "SoKD", "Konrektor einer Landesschule", "Konrektor einer Landesschule"), //
	SemR("084", "SemR", "Seminarrektor", "Seminarrektor"), //
	RSR("087", "RSR", "Realschulrektor", "Realschulrektor"), //
	ZwRSK("088", "ZwRSK", "Zweiter Realschulkonrektor", "Zweiter Realschulkonrektor"), //
	IR("089", "IR", "Institutsrektor", "Institutsrektor"), //
	IKR("090", "IKR", "Institutskonrektor", "Institutskonrektor"), //
	KR_FS("091", "KR(FS)", "Fachschulkonrektor", "Fachschulkonrektor"), //
	SD("092", "SD", "Sonderschuldirektor", "Sonderschuldirektor"), //
	WA("093", "WA", "Werkstattausbilder (FOS)", "Werkstattausbilder (FOS)"), //
	ID("094", "ID", "Institutsdirektor", "Institutsdirektor"), //
	HFL("096", "HFL", "Heilpädagogischer Förderlehrer", "Heilpädagogischer Förderlehrer"), //
	HPU("097", "HPU", "Heilpäd. Unterrichtshilfe", "Heilpädagogische Unterrichtshilfe"), //
	FL_BV("107", "FL BV", "Fachlehrer i.BV", "Fachlehrer im Beschäftigungsverhältnis"), //
	FöL_BV("110", "FöL BV", "Förderlehrer i.BV", "Förderlehrer im Beschäftigungsverhältnis"), //
	FOL_BV("113", "FOL BV", "Fachoberlehrer i.BV", "Fachoberlehrer im Beschäftigungsverhältnis"), //
	L_BV("121", "L BV", "Lehrer i.BV", "Lehrer im Beschäftigungsverhältnis"), //
	OL_i_BV("123", "OL i.BV", "Oberlehrer i.BV", "Oberlehrer im Beschäftigungsverhältnis"), //
	_2KR_BV("126", "2.KR BV", "Zw. Konrektor i.BV", "Zweiter Konrektor im Beschäftigungsverhältnis"), //
	KR_BV("128", "KR BV", "Konrektor i.BV", "Konrektor im Beschäftigungsverhältnis"), //
	BerR_BV("137", "BerR BV", "Beratungsrektor i.BV", "Beratungsrektor im Beschäftigungsverhältnis"), //
	SKR_BV("140", "SKR BV", "Sonderschulkonrektor i.BV", "Sonderschulkonrektor im Beschäftigungsverhältnis"), //
	_2SKR_BV("141", "2.SKR BV", "Zw. Sonderschulkonrektor i.BV", "Zweiter Sonderschulkonrektor im Beschäftigungsverhältnis"), //
	R_BV("144", "R BV", "Rektor i.BV", "Rektor im Beschäftigungsverhältnis"), //
	R_FS_BV("149", "R (FS) BV", "Fachschulrektor i.BV", "Fachschulrektor im Beschäftigungsverhältnis"), //
	StR_GS_BV("151", "StR GS BV", "Studienrat i.GSD i.BV", "Studienrat im Grundschuldienst im Beschäftigungsverhältnis"), //
	StR_MS_BV("152", "StR MS BV", "Studienrat i.MSD i.BV", "Studienrat im Mittelschuldienst im Beschäftigungsverhältnis"), //
	StR_FoeS_BV("153", "StR FöS BV", "Studienrat i.FöSD i.BV", "Studienrat im Förderschuldienst im Beschäftigungsverhältnis"), //
	StR_RS_BV("154", "StR RS BV", "Studienrat i.RSD i.BV", "Studienrat im Realschuldienst im Beschäftigungsverhältnis"), //
	SR_BV("157", "SR BV", "Sonderschulrektor i.BV", "Sonderschulrektor im Beschäftigungsverhältnis"), //
	StR_BV("161", "StR BV", "Studienrat i.BV", "Studienrat im Beschäftigungsverhältnis"), //
	OStR_BV("169", "OStR BV", "Oberstudienrat i.BV", "Oberstudienrat im Beschäftigungsverhältnis"), //
	StD_BV("171", "StD BV", "Studiendirektor i.BV", "Studiendirektor im Beschäftigungsverhältnis"), //
	SemR_BV("184", "SemR BV", "Seminarrektor i.BV", "Seminarrektor im Beschäftigungsverhältnis"), //
	ZwRSK_BV("188", "ZwRSK BV", "Zw. Realschulkonrektor i.BV", "Zweiter Realschulkonrektor im Beschäftigungsverhältnis"), //
	Beschaeft("200", "Beschäft.", "Beschäftigter", "Beschäftigter"), //
	Dia("201", "Dia", "Diakon", "Diakon"), //
	Pf("202", "Pf", "Pfarrer", "Pfarrer"), //
	Kt("203", "Kt", "Katechet", "Katechet"), //
	Ka("204", "Ka", "Kaplan", "Kaplan"), //
	Vi("205", "Vi", "Vikar (ev. Geistlicher)", "Vikar (evangelischer Geistlicher)"), //
	Rl("206", "Rl", "Relig.lehrer im Kirchendienst", "Religionslehrer im Kirchendienst"), //
	FL_iP("207", "FL i.P.", "Fachlehrer i.P.", "Fachlehrer im Privatschuldienst"), //
	FoeL_iP("210", "FöL i.P.", "Förderlehrer i.P.", "Förderlehrer im Privatschuldienst"), //
	DPsy("211", "DPsy", "Diplom-Psychologe", "Diplom-Psychologe"), //
	DSoPäd("212", "DSoPäd", "Sonderpädagoge (MA/Diplom)", "Sonderpädagoge (MA/Diplom)"), //
	FOL_iP("213", "FOL i.P.", "Fachoberlehrer i.P.", "Fachoberlehrer im Privatschuldienst"), //
	DPaed("214", "DPäd", "Pädagoge (MA/Diplom)", "Pädagoge (MA/Diplom)"), //
	Paed("215", "Päd", "Pädagoge (BA/graduiert)", "Pädagoge (BA/graduiert)"), //
	SoPaed("216", "SoPäd", "Sonderpädagoge (BA/graduiert)", "Sonderpädagoge (BA/graduiert)"), //
	L_iP("221", "L i.P.", "Lehrer i.P.", "Lehrer im Privatschuldienst"), //
	OL_iP("223", "OL i.P.", "Oberlehrer i.P.", "Oberlehrer im Privatschuldienst"), //
	ZwKR_iP("226", "ZwKR i.P.", "Zw. Konrektor i.P.", "Zweiter Konrektor im Privatschuldienst"), //
	KR_iP("228", "KR i.P.", "Konrektor i.P.", "Konrektor im Privatschuldienst"), //
	BerR_iP("237", "BerR i.P.", "Beratungsrektor i.P.", "Beratungsrektor im Privatschuldienst"), //
	SoKR_iP("240", "SoKR i.P.", "Sonderschulkonrektor i.P.", "Sonderschulkonrektor im Privatschuldienst"), //
	ZwSoK_iP("241", "ZwSoK i.P.", "Zw. Sonderschulkonrektor i.P.", "Zweiter Sonderschulkonrektor im Privatschuldienst"), //
	R_iP("244", "R i.P.", "Rektor i.P.", "Rektor im Privatschuldienst"), //
	R_FS_iP("249", "R(FS) i.P.", "Fachschulrektor i.P.", "Fachschulrektor im Privatschuldienst"), //
	StR_GS_iP("251", "StR(GS) iP", "Studienrat i.GSD i.P.", "Studienrat im Grundschuldienst im Privatschuldienst"), //
	StR_MS_iP("252", "StR(MS) iP", "Studienrat i.MSD i.P.", "Studienrat im Mittelschuldienst im Privatschuldienst"), //
	StR_FoeS_iP("253", "StR(FöS)iP", "Studienrat i.FöSD i.P.", "Studienrat im Förderschuldienst im Privatschuldienst"), //
	StR_RS_iP("254", "StR(RS) iP", "Studienrat i.RSD i.P.", "Studienrat im Realschuldienst im Privatschuldienst"), //
	SoR_iP("257", "SoR i.P.", "Sonderschulrektor i.P.", "Sonderschulrektor im Privatschuldienst"), //
	StR_iP("261", "StR i.P.", "Studienrat i.P.", "Studienrat im Privatschuldienst"), //
	OStR_iP("269", "OStR i.P.", "Oberstudienrat i.P.", "Oberstudienrat im Privatschuldienst"), //
	StD_iP("271", "StD i.P.", "Studiendirektor i.P.", "Studiendirektor im Privatschuldienst"), //
	ZwRSK_iP("288", "ZwRSK i.P.", "Zw. Realschulkonrektor i.P.", "Zweiter Realschulkonrektor im Privatschuldienst"), //
	FL_iK("307", "FL i.K.", "Fachlehrer i.K.", "Fachlehrer im Kirchendienst"), //
	FoeL_iK("310", "FöL i.K.", "Förderlehrer i.K.", "Förderlehrer im Kirchendienst"), //
	FOL_iK("313", "FOL i.K.", "Fachoberlehrer i.K.", "Fachoberlehrer im Kirchendienst"), //
	L_iK("321", "L i.K.", "Lehrer i.K.", "Lehrer im Kirchendienst"), //
	OL_iK("323", "OL i.K.", "Oberlehrer i.K.", "Oberlehrer im Kirchendienst"), //
	ZwKR_iK("326", "ZwKR i.K.", "Zw. Konrektor i.K.", "Zweiter Konrektor im Kirchendienst"), //
	KR_iK("328", "KR i.K.", "Konrektor i.K.", "Konrektor im Kirchendienst"), //
	BerR_iK("337", "BerR i.K.", "Beratungsrektor i.K.", "Beratungsrektor im Kirchendienst"), //
	SoKR_iK("340", "SoKR i.K.", "Sonderschulkonrektor i.K.", "Sonderschulkonrektor im Kirchendienst"), //
	ZwSoK_iK("341", "ZwSoK i.K.", "Zw. Sonderschulkonrektor i.K.", "Zweiter Sonderschulkonrektor im Kirchendienst"), //
	R_iK("344", "R i.K.", "Rektor i.K.", "Rektor im Kirchendienst"), //
	R_FS_iK("349", "R(FS) i.K.", "Fachschulrektor i.K.", "Fachschulrektor im Kirchendienst"), //
	StR_GS_iK("351", "StR(GS) iK", "Studienrat i.GSD i.K.", "Studienrat im Grundschuldienst im Kirchendienst"), //
	StR_MS_iK("352", "StR(MS) iK", "Studienrat i.MSD i.K.", "Studienrat im Mittelschuldienst im Kirchendienst"), //
	StR_FoeS_iK("353", "StR(FöS)iK", "Studienrat i.FöSD i.K.", "Studienrat im Förderschuldienst im Kirchendienst"), //
	StR_RS_iK("354", "StR(RS) iK", "Studienrat i.RSD i.K.", "Studienrat im Realschuldienst im Kirchendienst"), //
	RSD_iK("355", "RSD i.K.", "Realschuldirektor i.K.", "Realschuldirektor im Kirchendienst"), //
	RSK_iK("356", "RSK i.K.", "Realschulkonrektor i.K.", "Realschulkonrektor im Kirchendienst"), //
	SoR_iK("357", "SoR i.K.", "Sonderschulrektor i.K.", "Sonderschulrektor im Kirchendienst"), //
	StR_iK("361", "StR i.K.", "Studienrat i.K.", "Studienrat im Kirchendienst"), //
	OStR_iK("369", "OStR i.K.", "Oberstudienrat i.K.", "Oberstudienrat im Kirchendienst"), //
	StD_iK("371", "StD i.K.", "Studiendirektor i.K.", "Studiendirektor im Kirchendienst"), //
	OStD_iK("372", "OStD i.K", "Oberstudiendirektor i.K.", "Oberstudiendirektor im Kirchendienst"), //
	SemR_iK("384", "SemR i.K.", "Seminarrektor i.K.", "Seminarrektor im Kirchendienst"), //
	RSR_iK("387", "RSR i.K.", "Realschulrektor i.K.", "Realschulrektor im Kirchendienst"), //
	ZwRSK_iK("388", "ZwRSK i.K.", "Zw. Realschulkonrektor i.K.", "Zweiter Realschulkonrektor im Kirchendienst"), //
	Ltd_LD("810", "Ltd.LD", "Ltd. Landwirtschaftsdirektor", "Ltd. Landwirtschaftsdirektor"), //
	LD("811", "LD", "Landwirtschaftsdirektor", "Landwirtschaftsdirektor"), //
	LOR("812", "LOR", "Landwirtschaftsoberrat", "Landwirtschaftsoberrat"), //
	LR("813", "LR", "Landwirtschaftsrat", "Landwirtschaftsrat"), //
	Ltd_HD("830", "Ltd.HD", "Ltd. Hauswirtschaftsdirektor", "Ltd. Hauswirtschaftsdirektor"), //
	HD("831", "HD", "Hauswirtschaftsdirektor", "Hauswirtschaftsdirektor"), //
	HOR("832", "HOR", "Hauswirtschaftsoberrat", "Hauswirtschaftsoberrat"), //
	HR("833", "HR", "Hauswirtschaftsrat", "Hauswirtschaftsrat"), //
	Ltd_FD("850", "Ltd.FD", "Ltd. Forstdirektor", "Ltd. Forstdirektor"), //
	FD("851", "FD", "Forstdirektor", "Forstdirektor"), //
	FOR("852", "FOR", "Forstoberrat", "Forstoberrat"), //
	FR("853", "FR", "Forstrat", "Forstsrat"), //
	FAR("855", "FAR", "Forstamtsrat", "Forstamtsrat"), //
	FA("856", "FA", "Forstamtmann", "Forstamtmann"), //
	FOI("857", "FOI", "Forstoberinspektor", "Forstoberinspektor"), //
	OAR("870", "OAR", "Oberamtsrat", "Oberamtsrat"), //
	so("999", "so", "sonstige", "sonstige"); //


	private String schluessel;
	private String kurzform;
	private String anzeigeform;
	private String langform;
	
	private ASVAkademischeGrade(String schluessel, String kurzform,
			String anzeigeform, String langform) {
		this.schluessel = schluessel;
		this.kurzform = kurzform;
		this.anzeigeform = anzeigeform;
		this.langform = langform;
	}
	
	public static ASVAkademischeGrade findBySchluessel(String schluessel)
			throws ParseASVDataException {

		if (schluessel == null) {
			return null;
		}

		for (ASVAkademischeGrade amtsbezeichnung : ASVAkademischeGrade.values()) {
			if (amtsbezeichnung.schluessel.equals(schluessel)) {
				return amtsbezeichnung;
			}
		}

		throw new ParseASVDataException(
				"Der Amtsbezeichnun mit Schlüssel " + schluessel + " ist nicht bekannt.");

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
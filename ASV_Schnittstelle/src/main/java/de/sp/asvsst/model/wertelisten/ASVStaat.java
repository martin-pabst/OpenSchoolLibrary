package de.sp.asvsst.model.wertelisten;

import de.sp.asvsst.ParseASVDataException;

public enum ASVStaat {
	PAL("53751", "PAL", "Palau", "Palau"), //
	SOL("52451", "SOL", "Salomonen", "Salomonen"), //
	STP("26821", "STP", "São Tomé und Príncipe", "São Tomé und Príncipe"), //
	PNG("53851", "PNG", "Papua-Neuguinea", "Papua-Neuguinea"), //
	NAU("53151", "NAU", "Nauru", "Nauru"), //
	NEP("45841", "NEP", "Nepal", "Nepal"), //
	G("23621", "G", "Gabun", "Gabun"), //
	WAN("23221", "WAN", "Nigeria", "Nigeria"), //
	FSM("54551", "FSM", "Mikronesien", "Föderierte Staaten von Mikronesien"), //
	MGL("45741", "MGL", "Mongolei", "Mongolei"), //
	MOC("25421", "MOC", "Mosambik", "Mosambik"), //
	AG("32031", "AG", "Antigua und Barbuda", "Antigua und Barbuda"), //
	GQ("27421", "GQ", "Äquatorialguinea", "Äquatorialguinea"), //
	BHT("42641", "BHT", "Bhutan", "Bhutan"), //
	WG("34031", "WG", "Grenada", "Grenada"), //
	RU("29121", "RU", "Burundi", "Burundi"), //
	LAR("24821", "LAR", "Libyen", "Libyen"), //
	AFG("42340", "AFG", "Afghanistan", "Afghanistan"), //
	ARK("39531", "ARK", "Antarktis", "Antarktis-Territorium"), //
	BRU("42941", "BRU", "Brunei", "Brunei Darussalam"), //
	WL("36631", "WL", "St. Lucia", "St. Lucia"), //
	WV("36931", "WV", "St. Vincent und die Grenadinen",
			"St. Vincent und die Grenadinen"), //
	VAN("53251", "VAN", "Vánúatú", "Vánúatú"), //
	UAE("46941", "UAE", "Vereinigte Arabische Emirate",
			"Vereinigte Arabische Emirate"), //
	CV("24221", "CV", "Kap Verde", "Kap Verde"), //
	Q("44741", "Q", "Katar", "Katar"), //
	KIR("53051", "KIR", "Kiribati", "Kiribati"), //
	COM("24421", "COM", "Komoren", "Komoren"), //
	TON("54151", "TON", "Tonga", "Tonga"), //
	KAN("37031", "KAN", "St. Kitts und Nevis", "St. Kitts und Nevis"), //
	RG("26121", "RG", "Guinea", "Guinea"), //
	GUY("32831", "GUY", "Guyana", "Guyana"), //
	HN("34731", "HN", "Honduras", "Honduras"), //
	SSD("27821", "SSD", "Südsudan", "Südsudan"), //
	SUD("27721", "SUD", "Sudan", "Sudan"), //
	RM("24921", "RM", "Madagaskar", "Madagaskar"), //
	MS("25321", "MS", "Mauritius", "Mauritius"), //
	WAL("27221", "WAL", "Sierra Leone", "Sierra Leone"), //
	RIM("23921", "RIM", "Mauretanien", "Mauretanien"), //
	CAM("26221", "CAM", "Kamerun", "Kamerun"), //
	RCB("24521", "RCB", "Kongo, Republik", "Republik Kongo"), //
	ZRE("24621", "ZRE", "Kongo, Dem. Republik", "Demokratische Republik Kongo"), //
	ZW("23321", "ZW", "Simbabwe", "Simbabwe"), //
	GUB("25921", "GUB", "Guinea-Bissau", "Guinea-Bissau"), //
	EAT("28221", "EAT", "Tansania", "Vereinigte Republik Tansania"), //
	TG("28321", "TG", "Togo", "Togo"), //
	SO("27321", "SO", "Somalia", "Somalia"), //
	SD("28121", "SD", "Swasiland", "Swasiland"), //
	EAU("28621", "EAU", "Uganda", "Uganda"), //
	COI("52751", "COI", "Cookinseln", "Cookinseln"), //
	NIU("53351", "NIU", "Niue", "Niue"), //
	TN("28520", "TN", "Tunesien", "Tunesien"), //
	SY("27121", "SY", "Seychellen", "Seychellen"), //
	TD("28421", "TD", "Tschad", "Tschad"), //
	TUV("54051", "TUV", "Tuvalu", "Tuvalu"), //
	EAK("24321", "EAK", "Kenia", "Kenia"), //
	UA("16611", "UA", "Ukraine", "Ukraine"), //
	IS("13611", "IS", "Island", "Island"), //
	TR("16311", "TR", "Türkei", "Türkei"), //
	SCV("16711", "SCV", "Vatikanstaat", "Vatikanstaat"), //
	Ohne("99790", "Ohne", "Staatenlos", "Staatenlos"), //
	U("99891", "U", "Ungeklärt", "Ungeklärt"), //
	HR("13010", "HR", "Kroatien", "Kroatien"), //
	LV("13910", "LV", "Lettland", "Lettland"), //
	EST("12710", "EST", "Estland", "Estland"), //
	P("15310", "P", "Portugal", "Portugal"), //
	NL("14810", "NL", "Niederlande", "Niederlande"), //
	GB("16810", "GB", "Vereinigtes Königreich", "Vereinigtes Königreich"), //
	H("16510", "H", "Ungarn", "Ungarn"), //
	E("16110", "E", "Spanien", "Spanien"), //
	I("13710", "I", "Italien", "Italien"), //
	GR("13410", "GR", "Griechenland", "Griechenland"), //
	SLO("13110", "SLO", "Slowenien", "Slowenien"), //
	SK("15510", "SK", "Slowakei", "Slowakei"), //
	CZ("16410", "CZ", "Tschechien", "Tschechische Republik"), //
	IRL("13510", "IRL", "Irland", "Irland"), //
	M("14510", "M", "Malta", "Malta"), //
	CY("18110", "CY", "Zypern", "Zypern"), //
	ET("28720", "ET", "Ägypten", "Ägypten"), //
	RUS("16011", "RUS", "Russische Föderation", "Russische Föderation"), //
	ETH("22520", "ETH", "Äthiopien", "Äthiopien"), //
	BIH("12211", "BIH", "Bosnien und Herzegowina", "Bosnien und Herzegowina"), //
	BW("22721", "BW", "Botsuana", "Botsuana"), //
	D("00010", "D", "Deutschland", "Bundesrepublik Deutschland"), //
	BF("25821", "BF", "Burkina Faso", "Burkina Faso"), //
	BG("12510", "BG", "Bulgarien", "Bulgarien"), //
	DZ("22120", "DZ", "Algerien", "Algerien"), //
	AL("12111", "AL", "Albanien", "Albanien"), //
	LS("22621", "LS", "Lesotho", "Lesotho"), //
	DY("22921", "DY", "Benin", "Benin"), //
	SN("26921", "SN", "Senegal", "Senegal"), //
	RWA("26521", "RWA", "Ruanda", "Ruanda"), //
	RO("15410", "RO", "Rumänien", "Rumänien"), //
	A("15110", "A", "Österreich", "Österreich"), //
	XK("15011", "XK", "Kosovo", "Kosovo"), //
	L("14310", "L", "Luxemburg", "Luxemburg"), //
	PL("15210", "PL", "Polen", "Polen"), //
	RSM("15611", "RSM", "San Marino", "San Marino"), //
	NAM("26721", "NAM", "Namibia", "Namibia"), //
	FL("14111", "FL", "Liechtenstein", "Liechtenstein"), //
	RN("25521", "RN", "Niger", "Niger"), //
	LT("14210", "LT", "Litauen", "Litauen"), //
	MK("14411", "MK", "Mazedonien", "Mazedonien"), //
	MNE("14011", "MNE", "Montenegro", "Montenegro"), //
	RNR("25721", "RNR", "Sambia", "Sambia"), //
	WS("54351", "WS", "Samoa", "Samoa"), //
	SRB("17011", "SRB", "Serbien", "Serbien"), //
	FJI("52651", "FJI", "Fidschi", "Fidschi"), //
	B("12410", "B", "Belgien", "Belgien"), //
	Ausl("99991", "Ausl", "Ausland", "Ausland"), //
	TL("48341", "TL", "Timor-Leste", "Timor-Leste"), //
	SGP("47441", "SGP", "Singapur", "Singapur"), //
	K("44641", "K", "Kambodscha", "Kambodscha"), //
	CI("23121", "CI", "Côte d`Ivoire", "Côte d`Ivoire"), //
	YAR("42141", "YAR", "Jemen", "Jemen"), //
	TJ("47041", "TJ", "Tadschikistan", "Tadschikistan"), //
	TM("47141", "TM", "Turkmenistan", "Turkmenistan"), //
	KS("45041", "KS", "Kirgisistan", "Kirgisistan"), //
	KZ("44441", "KZ", "Kasachstan", "Kasachstan"), //
	AZ("42541", "AZ", "Aserbaidschan", "Aserbaidschan"), //
	BRN("42441", "BRN", "Bahrain", "Bahrain"), //
	BD("46041", "BD", "Bangladesch", "Bangladesch"), //
	AM("42241", "AM", "Armenien", "Armenien"), //
	MAL("48241", "MAL", "Malaysia", "Malaysia"), //
	SA("47241", "SA", "Saudi-Arabien", "Saudi-Arabien"), //
	LAO("44941", "LAO", "Laos", "Demokratische Republik Laos"), //
	NZ("53650", "NZ", "Neuseeland", "Neuseeland"), //
	J("44240", "J", "Japan", "Japan"), //
	RC("47940", "RC", "China", "Republik China (einschl. Hongkong)"), //
	HKJ("44540", "HKJ", "Jordanien", "Jordanien"), //
	IRQ("43840", "IRQ", "Irak", "Irak"), //
	NOK("43440", "NOK", "Korea, Dem. Volksrepublik",
			"Demokratische Volksrepublik Korea"), //
	T("47640", "T", "Thailand", "Thailand"), //
	CL("43140", "CL", "Sri Lanka", "Sri Lanka"), //
	SYR("47540", "SYR", "Syrien", "Arabische Republik Syrien"), //
	IL("44140", "IL", "Israel", "Israel"), //
	IR("43940", "IR", "Iran", "Islamische Republik Iran"), //
	RI("43740", "RI", "Indonesien", "Indonesien"), //
	ROK("46740", "ROK", "Korea, Republik", "Republik Korea"), //
	RP("46240", "RP", "Philippinen", "Philippinen"), //
	PK("46140", "PK", "Pakistan", "Pakistan"), //
	RL("45140", "RL", "Libanon", "Libanon"), //
	F("12910", "F", "Frankreich", "Frankreich"), //
	UZ("47741", "UZ", "Usbekistan", "Usbekistan"), //
	MD("14611", "MD", "Moldawien", "Republik Moldau"), //
	ANG("22321", "ANG", "Angola", "Angola"), //
	AUS("52350", "AUS", "Australien", "Australien"), //
	DJI("23021", "DJI", "Dschibuti", "Dschibuti"), //
	BUR("42741", "BUR", "Myanmar", "Myanmar"), //
	GBZ("19511", "GBZ", "Gibraltar", "Gibraltar"), //
	OM("45641", "OM", "Oman", "Oman"), //
	MC("14711", "MC", "Monaco", "Monaco"), //
	KWT("44841", "KWT", "Kuwait", "Kuwait"), //
	RCA("28921", "RCA", "Zentralafrikanische Republik",
			"Zentralafrikanische Republik"), //
	GE("43041", "GE", "Georgien", "Georgien"), //
	RMM("25121", "RMM", "Mali", "Mali"), //
	FIN("12810", "FIN", "Finnland", "Finnland"), //
	WAG("23721", "WAG", "Gambia", "Gambia"), //
	MW("25621", "MW", "Malawi", "Malawi"), //
	BY("16911", "BY", "Weißrussland", "Weißrussland"), //
	VN("43240", "VN", "Vietnam", "Vietnam"), //
	GH("23820", "GH", "Ghana", "Ghana"), //
	LB("24721", "LB", "Liberia", "Liberia"), //
	S("15710", "S", "Schweden", "Schweden"), //
	DK("12610", "DK", "Dänemark", "Dänemark und Färöer"), //
	IND("43640", "IND", "Indien", "Indien"), //
	MH("54451", "MH", "Marshallinseln", "Marshallinseln"), //
	N("14911", "N", "Norwegen", "Norwegen"), //
	CDN("34830", "CDN", "Kanada", "Kanada"), //
	BR("32730", "BR", "Brasilien", "Brasilien"), //
	RCH("33230", "RCH", "Chile", "Chile"), //
	USA("36830", "USA", "Vereinigte Staaten von Amerika",
			"Vereinigte Staaten von Amerika"), //
	MA("25220", "MA", "Marokko", "Marokko"), //
	AND("12311", "AND", "Andorra", "Andorra"), //
	WD("33331", "WD", "Dominica", "Dominica"), //
	ZA("26321", "ZA", "Südafrika", "Südafrika"), //
	CO("34931", "CO", "Kolumbien", "Kolumbien"), //
	CU("35131", "CU", "Kuba", "Kuba"), //
	DOM("33531", "DOM", "Dominikanische Republik", "Dominikanische Republik"), //
	ROU("36531", "ROU", "Uruguay", "Uruguay"), //
	SME("36431", "SME", "Suriname", "Suriname"), //
	JA("35531", "JA", "Jamaika", "Jamaika"), //
	GCA("34531", "GCA", "Guatemala", "Guatemala"), //
	RH("34631", "RH", "Haiti", "Haiti"), //
	YV("36731", "YV", "Venezuela", "Venezuela"), //
	TT("37131", "TT", "Trinidad und Tobago", "Trinidad und Tobago"), //
	ES("33731", "ES", "El Salvador", "El Salvador"), //
	BOL("32631", "BOL", "Bolivien", "Bolivien"), //
	BS("32431", "BS", "Bahamas", "Bahamas"), //
	RA("32331", "RA", "Argentinien", "Argentinien"), //
	BDS("32231", "BDS", "Barbados", "Barbados"), //
	BH("33031", "BH", "Belize", "Belize"), //
	PY("35931", "PY", "Paraguay", "Paraguay"), //
	PA("35731", "PA", "Panama", "Panama"), //
	MEX("35331", "MEX", "Mexiko", "Mexiko"), //
	PE("36131", "PE", "Peru", "Peru"), //
	CR("33431", "CR", "Costa Rica", "Costa Rica"), //
	NIC("35431", "NIC", "Nicaragua", "Nicaragua"), //
	EC("33631", "EC", "Ecuador", "Ecuador"), //
	CH("15811", "CH", "Schweiz", "Schweiz"), //
	MV("45441", "MV", "Malediven", "Malediven"), //
	ER("22421", "ER", "Eritrea", "Eritrea"); //

	private String schluessel;
	private String kurzform;
	private String anzeigeform;
	private String langform;

	private ASVStaat(String schluessel, String kurzform, String anzeigeform,
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

	public static ASVStaat findBySchluessel(String schluessel)
			throws ParseASVDataException {

		if (schluessel == null) {
			return null;
		}

		for (ASVStaat staat : ASVStaat.values()) {
			if (staat.schluessel.equals(schluessel)) {
				return staat;
			}
		}

		throw new ParseASVDataException(
				"Der Staat mit Schlüssel " + schluessel + " ist nicht bekannt.");

	}

}
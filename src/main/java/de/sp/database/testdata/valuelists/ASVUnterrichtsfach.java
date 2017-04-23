package de.sp.database.testdata.valuelists;

import de.sp.database.valuelists.ValueNotFoundException;


public enum ASVUnterrichtsfach {
	aae("0200600400", "aae", "Archäologie", false, false), //
	Alb("0100100100", "Alb", "Albanisch", false, true), //
	AnM("0300400500", "AnM", "Angewandte Mathematik", false, false), //
	Ara("0100200100", "Ara", "Arabisch", false, true), //
	B("0300100100", "B", "Biologie", false, false), //
	bcp("0300600300", "bcp", "Biol.-chem. Praktikum", false, false), //
	Bos("0100300100", "Bos", "Bosnisch", false, true), //
	BÜ("0300100300", "BÜ", "Biologie (Übungen)", false, false), //
	C("0300200100", "C", "Chemie", false, false), //
	can("0300200400", "can", "Chemische Analyse", false, false), //
	Chi("0100400100", "Chi", "Chinesisch", false, true), //
	Cho("0400200400", "Cho", "Chor", false, false), //
	Chw("0400200500", "Chw", "Chor (WiU)", false, false), //
	CÜ("0300200200", "CÜ", "Chemie (Übungen)", false, false), //
	G("0200600100", "G", "Geschichte", false, false), //
	GPlusS("0201001200", "G+S", "Geschichte + Sozialkunde", false, false), //
	Geo("0200500200", "Geo", "Geografie", false, false), //
	glg("0200500400", "glg", "Geologie", false, false), //
	Gr("0100800100", "Gr", "Griechisch", false, true), //
	GSZ("0400100400", "GSZ", "gebundenes Sachzeichnen", false, false), //
	GU("0300600500", "GU", "Geoökologie und Umweltschutz", false, false), //
	heb("0100900100", "heb", "Hebräisch", false, true), //
	IL("0700203200", "IL", "Individuelle Lernzeit", false, false), //
	Hw("0500300300", "Hw", "Hauswirtschaft", false, false), //
	IF("0700202900", "IF", "Individuelle Förderung", false, false), //
	Inf("0300300100", "Inf", "Informatik", false, false), //
	InfA("0300300800", "InfA", "Angewandte Informatik", false, false), //
	Ins("0400200900", "Ins", "Instrumentalunterricht", false, false), //
	INS("0400200800", "INS", "InsE (WiU, Quali. Oberstufe)", false, false), //
	Isr("0200300500", "Isr", "Israelitische Religionslehre", true, false), //
	IsU("0200300300", "IsU", "Islamischer Unterricht", true, false), //
	It("0101000100", "It", "Italienisch", false, true), //
	IT("0300301100", "IT", "Informationstechnologie", false, false), //
	Its("0101000200", "Its", "Italienisch spätbeginnend", false, true), //
	jap("0101100100", "jap", "Japanisch", false, true), //
	K("0200100100", "K", "Katholische Religionslehre", true, false), //
	Kon("0102500200", "Kon", "sonst. fremdspr. Konversation", false, true), //
	Kro("0101200100", "Kro", "Kroatisch", false, true), //
	Ks("0500100500", "Ks", "Kurzschrift", false, false), //
	Kse("0500100600", "Kse", "Kurzschrift u engl. Kurzschr.", false, false), //
	Ku("0400100100", "Ku", "Kunst", false, false), //
	KuA("0400101400", "KuA", "Architektur", false, false), //
	KuB("0400101300", "KuB", "Bildnerische Praxis", false, false), //
	kug("0400101000", "kug", "Kunstgeschichte", false, false), //
	KuM("0400101600", "KuM", "Film- und Mediendesign", false, false), //
	KuP("0400101500", "KuP", "Produktdesign", false, false), //
	L("0101300100", "L", "Latein", false, true), //
	LeL("0700201400", "LeL", "Lernen Lernen", false, false), //
	lin("0100501500", "lin", "Linguistik", false, false), //
	lit("0100501600", "lit", "Literatur", false, false), //
	M_CAS("0300401400", "M-CAS", "Mathematik CAS", false, false), //
	M_R("0300401300", "M-R", "Reine Mathematik", false, false), //
	min("0300600700", "min", "Mineralogie", false, false), //
	mnt("0300600800", "mnt", "sonst. ZA (math/nat/techn)", false, false), //
	Ms("0500100800", "Ms", "Maschinenschreiben", false, false), //
	Mu("0400200100", "Mu", "Musik", false, false), //
	MuI("0400201300", "MuI", "Instrument", false, false), //
	NGr("0100800200", "NGr", "Neugriechisch", false, true), //
	Nls("0102700100", "Nls", "Niederländisch", false, true), //
	NR("0200300600", "NR", "Neuapostolische Religionslehre", true, false), //
	NuT("0300600200", "NuT", "Natur und Technik", false, false), //
	Orc("0400202100", "Orc", "Orchester", false, false), //
	Ort("0200300700", "Ort", "Orthodoxe Religionslehre", true, false), //
	Orw("0400202200", "Orw", "Orchester (WiU)", false, false), //
	pae("0200700100", "pae", "Pädagogik", false, false), //
	Ph("0300500100", "Ph", "Physik", false, false), //
	PhA("0300501100", "PhA", "Astrophysik (Quali. Oberstufe)", false, false), //
	PhB("0300500600", "PhB", "Biophysik (Quali. Oberstufe)", false, false), //
	Phi("0200800100", "Phi", "Philosophie", false, false), //
	PhÜ("0300500900", "PhÜ", "Physik (Übungen)", false, false), //
	Pln("0101500100", "Pln", "Polnisch", false, true), //
	Po("0101600100", "Po", "Portugiesisch", false, true), //
	PrA("0700201700", "PrA", "Projektarbeit", false, false), //
	psy("0200700300", "psy", "Psychologie", false, false), //
	PZG("0200900200", "PZG", "Politik und Zeitgeschichte", false, false), //
	rhe("0100501700", "rhe", "Rhetorik", false, false), //
	Rk("0500400900", "Rk", "Rechtskunde", false, false), //
	Rl("0200301400", "Rl", "Sonstige Religionslehre", true, false), //
	M("0300400100", "M", "Mathematik", false, false), //
	Rw("0500400700", "Rw", "Rechnungswesen", false, false), //
	S_P("0400302000", "S-P", "Sportpraxis (Quali. Oberstufe)", false, false), //
	S_T("0400301800", "S-T", "Sporttheorie (Quali. Oberst.)", false, false), //
	Ser("0101800100", "Ser", "Serbisch", false, true), //
	SF("0400301400", "SF", "Sportförderunterricht", false, false), //
	Sga("0700203800", "Sga", "Schulgarten", false, false), //
	Sk("0200900100", "Sk", "Sozialkunde", false, false), //
	Skd("0400300900", "Skd", "Diff. Sportunterricht (mw)", false, false), //
	Ske("0400301900", "Ske", "Erweiterter Basissport (mw)", false, false), //
	Skr("0101900100", "Skr", "Serbokroatisch", false, true), //
	slk("0400400700", "slk", "sonst ZA (sprach/lit/kunst)", false, false), //
	Slo("0102000100", "Slo", "Slowenisch", false, true), //
	Sm("0400300200", "Sm", "Sport (m)", false, false), //
	Smd("0400300700", "Smd", "Diff. Sportunterricht (m)", false, false), //
	Sme("0400300500", "Sme", "Erweiterter Basissport (m)", false, false), //
	Smw("0400300400", "Smw", "Sport (mw)", false, false), //
	snw("0700202200", "snw", "sonstiges Fach (nichtwiss)", false, false), //
	SoG("0200900300", "SoG", "Sozialpraktische Grundbildung", false, false), //
	son("0700202400", "son", "sonstiges Fach (wiss)", false, false), //
	Sp("0102100100", "Sp", "Spanisch", false, true), //
	Spr("0700201900", "Spr", "Schulprofil", false, false), //
	Sps("0102100200", "Sps", "Spanisch spätbeginnend", false, true), //
	SSp("0400400500", "SSp", "Schulspiel", false, false), //
	Sw("0400300300", "Sw", "Sport (w)", false, false), //
	SwA("0201000800", "SwA", "Sozialwiss. Arbeitsfelder", false, false), //
	Swd("0400300800", "Swd", "Diff. Sportunterricht (w)", false, false), //
	Swe("0400300600", "Swe", "Erweiterter Basissport (w)", false, false), //
	TaW("0500300600", "TaW", "Textilarbeit mit Werken", false, false), //
	Ts("0102200100", "Ts", "Tschechisch", false, true), //
	TR("0102300100", "TR", "Türkisch", false, true), //
	TuF("0700100400", "TuF", "Theater und Film", false, false), //
	Tv("0500101400", "Tv", "Textverarbeitung", false, false), //
	TZ("0500100900", "TZ", "Technisches Zeichnen", false, false), //
	Ung("0102400100", "Ung", "Ungarisch", false, true), //
	VOC("0400202400", "VOC", "VokalE (WiU, Quali. Oberstufe)", false, false), //
	We("0500300800", "We", "Werken", false, false), //
	WiE("0100600700", "WiE", "Wirtschaftsenglisch", false, false), //
	WIn("0500401600", "WIn", "Wirtschaftsinformatik", false, false), //
	WR("0500401200", "WR", "Wirtschaft und Recht", false, false), //
	WSp("0500101800", "WSp", "Wirtsch.-sprache/techn.Sprache", false, false), //
	ztg("0200600300", "ztg", "Zeitgeschichte", false, false), //
	D("0100500100", "D", "Deutsch", false, false), //
	DFr("0100500700", "DFr", "Deutsch als Fremdsprache", false, false), //
	DSp("0400400800", "DSp", "Darstellendes Spiel", false, false), //
	E("0100600100", "E", "Englisch", false, true), //
	EH("0700200600", "EH", "Erste Hilfe", false, false), //
	EKo("0100600300", "EKo", "Englisch Konversation", false, false), //
	Eth("0200400100", "Eth", "Ethik", true, false), //
	Ev("0200200100", "Ev", "Evangelische Religionslehre", true, false), //
	F("0100700100", "F", "Französisch", false, true), //
	FKo("0100700200", "FKo", "Französisch Konversation", false, false), //
	Fot("0400500100", "Fot", "Fotografie", false, false), //
	FPB("0102500500", "FPB", "Befreiung fremdspr. PU/WPU", false, false), //
	Fs("0100700400", "Fs", "Französisch spätbeginnend", false, true), //
	Fsp("0102500100", "Fsp", "sonstige Fremdsprache", false, false), //
	Ru("0101700100", "Ru", "Russisch", false, true), //
	Rus("0101700200", "Rus", "Russisch spätbeginnend", false, true); //

	private String schluessel;
	private String kurzform;
	private String anzeigeform;
	private boolean is_religion;
	private boolean is_language;

	ASVUnterrichtsfach(String schluessel, String kurzform, String anzeigeform,
					   boolean is_religion, boolean is_language) {
		this.schluessel = schluessel;
		this.kurzform = kurzform;
		this.anzeigeform = anzeigeform;
		this.is_religion = is_religion;
		this.is_language = is_language;
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

	public static ASVUnterrichtsfach findBySchluessel(String schluessel)
			throws ValueNotFoundException {

		if (schluessel == null) {
			return null;
		}

		for (ASVUnterrichtsfach staat : ASVUnterrichtsfach.values()) {
			if (staat.schluessel.equals(schluessel)) {
				return staat;
			}
		}

		throw new ValueNotFoundException("Das ASV-Fach mit Schlüssel "
				+ schluessel + " ist nicht bekannt.");

	}

    public boolean isReligion() {
        return is_religion;
    }

    public boolean isLanguage() {
        return is_language;
    }
}
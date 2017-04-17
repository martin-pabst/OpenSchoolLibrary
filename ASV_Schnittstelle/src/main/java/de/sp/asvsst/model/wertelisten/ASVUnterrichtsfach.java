package de.sp.asvsst.model.wertelisten;

import de.sp.asvsst.ParseASVDataException;

public enum ASVUnterrichtsfach {
	aae("0200600400", "aae", "Archäologie"), //
	Alb("0100100100", "Alb", "Albanisch"), //
	AnM("0300400500", "AnM", "Angewandte Mathematik"), //
	Ara("0100200100", "Ara", "Arabisch"), //
	B("0300100100", "B", "Biologie"), //
	bcp("0300600300", "bcp", "Biol.-chem. Praktikum"), //
	Bos("0100300100", "Bos", "Bosnisch"), //
	BÜ("0300100300", "BÜ", "Biologie (Übungen)"), //
	C("0300200100", "C", "Chemie"), //
	can("0300200400", "can", "Chemische Analyse"), //
	Chi("0100400100", "Chi", "Chinesisch"), //
	Cho("0400200400", "Cho", "Chor"), //
	Chw("0400200500", "Chw", "Chor (WiU)"), //
	CÜ("0300200200", "CÜ", "Chemie (Übungen)"), //
	FspSem("0102500800", "FspSem", "sonstige Fremdsprache (Sem.)"), //
	G("0200600100", "G", "Geschichte"), //
	GPlusS("0201001200", "G+S", "Geschichte + Sozialkunde"), //
	Geo("0200500200", "Geo", "Geografie"), //
	glg("0200500400", "glg", "Geologie"), //
	Gr("0100800100", "Gr", "Griechisch"), //
	GrSem("0100800800", "GrSem", "Griechisch (Seminarleitfach)"), //
	GSZ("0400100400", "GSZ", "gebundenes Sachzeichnen"), //
	GU("0300600500", "GU", "Geoökologie und Umweltschutz"), //
	heb("0100900100", "heb", "Hebräisch"), //
	IL("0700203200", "IL", "Individuelle Lernzeit"), //
	Hw("0500300300", "Hw", "Hauswirtschaft"), //
	IF("0700202900", "IF", "Individuelle Förderung"), //
	Inf("0300300100", "Inf", "Informatik"), //
	InfA("0300300800", "InfA", "Angewandte Informatik"), //
	Ins("0400200900", "Ins", "Instrumentalunterricht"), //
	INS("0400200800", "INS", "InsE (WiU, Quali. Oberstufe)"), //
	Isr("0200300500", "Isr", "Israelitische Religionslehre"), //
	IsU("0200300300", "IsU", "Islamischer Unterricht"), //
	It("0101000100", "It", "Italienisch"), //
	IT("0300301100", "IT", "Informationstechnologie"), //
	Its("0101000200", "Its", "Italienisch spätbeginnend"), //
	ItSem("0101000800", "ItSem", "Italienisch (Seminarleitfach)"), //
	jap("0101100100", "jap", "Japanisch"), //
	K("0200100100", "K", "Katholische Religionslehre"), //
	Kon("0102500200", "Kon", "sonst. fremdspr. Konversation"), //
	Kro("0101200100", "Kro", "Kroatisch"), //
	Ks("0500100500", "Ks", "Kurzschrift"), //
	Kse("0500100600", "Kse", "Kurzschrift u engl. Kurzschr."), //
	Ku("0400100100", "Ku", "Kunst"), //
	KuA("0400101400", "KuA", "Architektur"), //
	KuB("0400101300", "KuB", "Bildnerische Praxis"), //
	kug("0400101000", "kug", "Kunstgeschichte"), //
	KuM("0400101600", "KuM", "Film- und Mediendesign"), //
	KuP("0400101500", "KuP", "Produktdesign"), //
	L("0101300100", "L", "Latein"), //
	LeL("0700201400", "LeL", "Lernen Lernen"), //
	lin("0100501500", "lin", "Linguistik"), //
	lit("0100501600", "lit", "Literatur"), //
	LSem("0101300800", "LSem", "Latein (Seminarleitfach)"), //
	M_CAS("0300401400", "M-CAS", "Mathematik CAS"), //
	M_R("0300401300", "M-R", "Reine Mathematik"), //
	min("0300600700", "min", "Mineralogie"), //
	mnt("0300600800", "mnt", "sonst. ZA (math/nat/techn)"), //
	Ms("0500100800", "Ms", "Maschinenschreiben"), //
	Mu("0400200100", "Mu", "Musik"), //
	MuI("0400201300", "MuI", "Instrument"), //
	NGr("0100800200", "NGr", "Neugriechisch"), //
	NGrSem("0100800900", "NGrSem", "Neugriechisch (Seminarfach)"), //
	Nls("0102700100", "Nls", "Niederländisch"), //
	NR("0200300600", "NR", "Neuapostolische Religionslehre"), //
	NuT("0300600200", "NuT", "Natur und Technik"), //
	Orc("0400202100", "Orc", "Orchester"), //
	Ort("0200300700", "Ort", "Orthodoxe Religionslehre"), //
	Orw("0400202200", "Orw", "Orchester (WiU)"), //
	pae("0200700100", "pae", "Pädagogik"), //
	Ph("0300500100", "Ph", "Physik"), //
	PhA("0300501100", "PhA", "Astrophysik (Quali. Oberstufe)"), //
	PhB("0300500600", "PhB", "Biophysik (Quali. Oberstufe)"), //
	Phi("0200800100", "Phi", "Philosophie"), //
	PhÜ("0300500900", "PhÜ", "Physik (Übungen)"), //
	Pln("0101500100", "Pln", "Polnisch"), //
	Po("0101600100", "Po", "Portugiesisch"), //
	PrA("0700201700", "PrA", "Projektarbeit"), //
	psy("0200700300", "psy", "Psychologie"), //
	PZG("0200900200", "PZG", "Politik und Zeitgeschichte"), //
	rhe("0100501700", "rhe", "Rhetorik"), //
	Rk("0500400900", "Rk", "Rechtskunde"), //
	Rl("0200301400", "Rl", "Sonstige Religionslehre"), //
	RuSem("0101700800", "RuSem", "Russisch (Seminarleitfach)"), //
	M("0300400100", "M", "Mathematik"), //
	Rw("0500400700", "Rw", "Rechnungswesen"), //
	S_P("0400302000", "S-P", "Sportpraxis (Quali. Oberstufe)"), //
	S_T("0400301800", "S-T", "Sporttheorie (Quali. Oberst.)"), //
	Ser("0101800100", "Ser", "Serbisch"), //
	SF("0400301400", "SF", "Sportförderunterricht"), //
	Sga("0700203800", "Sga", "Schulgarten"), //
	Sk("0200900100", "Sk", "Sozialkunde"), //
	Skd("0400300900", "Skd", "Diff. Sportunterricht (mw)"), //
	Ske("0400301900", "Ske", "Erweiterter Basissport (mw)"), //
	Skr("0101900100", "Skr", "Serbokroatisch"), //
	slk("0400400700", "slk", "sonst ZA (sprach/lit/kunst)"), //
	Slo("0102000100", "Slo", "Slowenisch"), //
	Sm("0400300200", "Sm", "Sport (m)"), //
	Smd("0400300700", "Smd", "Diff. Sportunterricht (m)"), //
	Sme("0400300500", "Sme", "Erweiterter Basissport (m)"), //
	Smw("0400300400", "Smw", "Sport (mw)"), //
	snw("0700202200", "snw", "sonstiges Fach (nichtwiss)"), //
	SoG("0200900300", "SoG", "Sozialpraktische Grundbildung"), //
	son("0700202400", "son", "sonstiges Fach (wiss)"), //
	Sp("0102100100", "Sp", "Spanisch"), //
	Spr("0700201900", "Spr", "Schulprofil"), //
	Sps("0102100200", "Sps", "Spanisch spätbeginnend"), //
	SpSem("0102100800", "SpSem", "Spanisch (Seminarleitfach)"), //
	SSp("0400400500", "SSp", "Schulspiel"), //
	Sw("0400300300", "Sw", "Sport (w)"), //
	SwA("0201000800", "SwA", "Sozialwiss. Arbeitsfelder"), //
	Swd("0400300800", "Swd", "Diff. Sportunterricht (w)"), //
	Swe("0400300600", "Swe", "Erweiterter Basissport (w)"), //
	TaW("0500300600", "TaW", "Textilarbeit mit Werken"), //
	Ts("0102200100", "Ts", "Tschechisch"), //
	TR("0102300100", "TR", "Türkisch"), //
	TuF("0700100400", "TuF", "Theater und Film"), //
	Tv("0500101400", "Tv", "Textverarbeitung"), //
	TZ("0500100900", "TZ", "Technisches Zeichnen"), //
	Ung("0102400100", "Ung", "Ungarisch"), //
	VOC("0400202400", "VOC", "VokalE (WiU, Quali. Oberstufe)"), //
	We("0500300800", "We", "Werken"), //
	WiE("0100600700", "WiE", "Wirtschaftsenglisch"), //
	WIn("0500401600", "WIn", "Wirtschaftsinformatik"), //
	WR("0500401200", "WR", "Wirtschaft und Recht"), //
	WSp("0500101800", "WSp", "Wirtsch.-sprache/techn.Sprache"), //
	ztg("0200600300", "ztg", "Zeitgeschichte"), //
	D("0100500100", "D", "Deutsch"), //
	DFr("0100500700", "DFr", "Deutsch als Fremdsprache"), //
	DSp("0400400800", "DSp", "Darstellendes Spiel"), //
	E("0100600100", "E", "Englisch"), //
	EH("0700200600", "EH", "Erste Hilfe"), //
	EKo("0100600300", "EKo", "Englisch Konversation"), //
	ESem("0100600800", "ESem", "Englisch (Seminarleitfach)"), //
	Eth("0200400100", "Eth", "Ethik"), //
	Ev("0200200100", "Ev", "Evangelische Religionslehre"), //
	F("0100700100", "F", "Französisch"), //
	FKo("0100700200", "FKo", "Französisch Konversation"), //
	Fot("0400500100", "Fot", "Fotografie"), //
	FPB("0102500500", "FPB", "Befreiung fremdspr. PU/WPU"), //
	Fs("0100700400", "Fs", "Französisch spätbeginnend"), //
	FSem("0100700800", "FSem", "Französisch (Seminarleitfach)"), //
	Fsp("0102500100", "Fsp", "sonstige Fremdsprache"), //
	Ru("0101700100", "Ru", "Russisch"), //
	Rus("0101700200", "Rus", "Russisch spätbeginnend"); //

	private String schluessel;
	private String kurzform;
	private String anzeigeform;

	private ASVUnterrichtsfach(String schluessel, String kurzform,
			String anzeigeform) {
		this.schluessel = schluessel;
		this.kurzform = kurzform;
		this.anzeigeform = anzeigeform;
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
			throws ParseASVDataException {

		if (schluessel == null) {
			return null;
		}

		for (ASVUnterrichtsfach staat : ASVUnterrichtsfach.values()) {
			if (staat.schluessel.equals(schluessel)) {
				return staat;
			}
		}

		throw new ParseASVDataException("Das ASV-Fach mit Schlüssel "
				+ schluessel + " ist nicht bekannt.");

	}

}
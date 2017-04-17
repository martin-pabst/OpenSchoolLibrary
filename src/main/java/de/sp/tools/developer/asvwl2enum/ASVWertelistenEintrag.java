package de.sp.tools.developer.asvwl2enum;

import java.util.Date;

import org.simpleframework.xml.Default;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "eintrag", strict = false)
@Default()
public class ASVWertelistenEintrag {

	public String schluessel;
	public String kurzform;
	public String anzeigeform;
	public String langform;

	@Element(required = false)
	public String bemerkung;

	public Date gueltig_von;

	@Element(required = false)
	public Date gueltig_bis;

	@Override
	public String toString() {

		return kurzform + "(\"" + schluessel + "\", \"" + kurzform + "\", \""
				+ anzeigeform + "\", \"" + langform + "\")";

	}

}

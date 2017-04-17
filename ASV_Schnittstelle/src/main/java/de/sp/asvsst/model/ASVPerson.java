package de.sp.asvsst.model;

import org.simpleframework.xml.Default;
import org.simpleframework.xml.DefaultType;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import de.sp.asvsst.model.asvwlstore.ASVWertelistenEintrag;
import de.sp.asvsst.model.asvwlstore.ASVWlStore;

@Root(name = "person")
@Default(DefaultType.FIELD)
public class ASVPerson {

	@Element(name = "personentyp", required = false)
	public String personentypSchluessel;

	@Element(required = false)
	public String familienname;

	@Element(required = false)
	public String vornamen;

	@Element(required = false)
	public String namensbestandteil_vorangestellt = "";

	@Element(required = false)
	public String namensbestandteil_nachgestellt = "";

	@Element(name = "geschlecht", required = false)
	public String geschlechtSchluessel;

	@Element(name = "anrede", required = false)
	public String anredeSchluessel;

	@Element(name = "akademischer_grad", required = false)
	public String akademischerGradSchluessel;

	@Element(name = "funktion_als_elternvertreter", required = false)
	public String funktionAlsElternvertreterSchluessel;

	@Element(required = false)
	public boolean ist_mitglied_schulforum;

	@Element(name = "staatsangehoerigkeit", required = false)
	public String staatSchluessel;

	@Element(required = false)
	public boolean isPrimaryLegalParent = false;

	@Element(required = false)
	public boolean isLegalParent = false;

	public boolean isMale() {

		if (geschlechtSchluessel != null && geschlechtSchluessel.equals("2")) {
			return false;
		}
		
		if(anredeSchluessel != null && anredeSchluessel.equals("2")){
			return false;
		}

		return true;
	}

	public String getNamensbestandteilVorangestelltNotNull(
			boolean withTrailingSpace) {

		if (namensbestandteil_vorangestellt == null
				|| namensbestandteil_vorangestellt.isEmpty()) {
			return "";
		}

		return withTrailingSpace ? namensbestandteil_vorangestellt + " "
				: namensbestandteil_vorangestellt;

	}

	public String getNamensbestandteilNachgestelltNotNull(
			boolean withLeadingSpace) {

		if (namensbestandteil_nachgestellt == null
				|| namensbestandteil_nachgestellt.isEmpty()) {
			return "";
		}

		return withLeadingSpace ? " " + namensbestandteil_nachgestellt
				: namensbestandteil_nachgestellt;
	}

	public String getAkadGradString(boolean withTrailingSpace) throws Exception {

		ASVWlStore wlStore = ASVWlStore.getInstance();

		ASVWertelistenEintrag asvAkadGrad = wlStore.findEintrag("2005",
				akademischerGradSchluessel);

		if (asvAkadGrad == null) {
			return "";
		}
		String akadGrad = asvAkadGrad.kurzform;

		if (withTrailingSpace) {
			akadGrad += " ";
		}

		return akadGrad;

	}

	public String getHerrFrau() {

		if (isMale()) {
			return "Herr";
		}

		return "Frau";

	}

	public String getHerrnFrau() {

		if (isMale()) {
			return "Herrn";
		}

		return "Frau";

	}

	public String getWholeName(boolean withFirstname) throws Exception {

		return getAkadGradString(true) 
				+ getNamensbestandteilVorangestelltNotNull(true) 
				+ (withFirstname ? vornamen + " " : "") 
				+ familienname
				+ getNamensbestandteilNachgestelltNotNull(true);

	}

	public String getHerrFrauWholeName() throws Exception {
		return getHerrFrau() + " " + getWholeName(false);
	}

	public String getHerrnFrauWholeName() throws Exception {
		return getHerrnFrau() + " " + getWholeName(true);
	}

	public String getBriefAnrede(boolean grossAmAnfang) throws Exception {

		String s = grossAmAnfang ? "S" : "s";

		if (geschlechtSchluessel != null && geschlechtSchluessel.equals("2")) {
			s += "ehr geehrte ";
		} else {
			s += "ehr geehrter ";
		}

		s += getHerrFrauWholeName();

		return s;

	}

}

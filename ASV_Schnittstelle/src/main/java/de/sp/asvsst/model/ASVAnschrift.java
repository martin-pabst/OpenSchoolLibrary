package de.sp.asvsst.model;

import java.util.ArrayList;
import java.util.List;

import org.simpleframework.xml.Default;
import org.simpleframework.xml.DefaultType;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import de.sp.asvsst.ParseASVDataException;
import de.sp.asvsst.model.asvwlstore.ASVWertelistenEintrag;
import de.sp.asvsst.model.asvwlstore.ASVWlStore;
import de.sp.asvsst.model.wertelisten.ASVAnschriftstyp;
import de.sp.asvsst.model.wertelisten.ASVStaat;

@Root(name = "anschrift")
@Default(DefaultType.FIELD)
public class ASVAnschrift {

	@Element(name = "anschriftstyp", required = false)
	public String anschriftstypSchluessel;
	@Element(name = "kennung", required = false)
	public String kennungSchluessel;

	@Element(required = false)
	public String strasse = "";
	@Element(required = false)
	public String nummer = "";
	@Element(required = false)
	public String postleitzahl = "";

	@Element(required = false)
	public String postfach;
	@Element(required = false)
	public String postleitzahl_postfach;

	@Element(required = false)
	public String ortsbezeichnung = "";
	@Element(required = false)
	public String ortsbezeichnung_zusatz = "";
	@Element(required = false)
	public String ortsteil;
	@Element(required = false)
	public String ortsteil_zusatz = "";
	@Element(required = false)
	public String anredetext = "";
	@Element(required = false)
	public String anschrifttext = "";

	@Element(name = "staat", required = false)
	public String staatSchluessel;
	@Element(required = false)
	public String gemeinde;

	@Element(required = false)
	public boolean im_verteiler_schriftverkehr;

	@ElementList(required = false)
	public List<ASVKommunikation> kommunikationsdaten = new ArrayList<ASVKommunikation>();

	public ASVStaat getStaat() throws ParseASVDataException {

		return ASVStaat.findBySchluessel(staatSchluessel);

	}

	public ASVAnschriftstyp getAnschriftsTyp() throws ParseASVDataException {

		return ASVAnschriftstyp.findBySchluessel(anschriftstypSchluessel);

	}

	@Override
	public boolean equals(Object o) {
		
		ASVAnschrift a = (ASVAnschrift)o;
		
		if(a == null){
			return false;
		}
		
		if(!se(strasse, a.strasse)){
			return false;
		}
		
		if(!se(nummer, a.nummer)){
			return false;
		}
		
		if(!se(postleitzahl, a.postleitzahl)){
			return false;
		}
		
		if(!se(ortsbezeichnung, a.ortsbezeichnung)){
			return false;
		}
		
		if(!se(postfach, a.postfach)){
			return false;
		}
		
		if(!se(postleitzahl_postfach, a.postleitzahl_postfach)){
			return false;
		}
		
		if(im_verteiler_schriftverkehr != a.im_verteiler_schriftverkehr){
			return false;
		}
		
		return true;
		
	}

	private boolean se(String s1, String s2) {

		if (s1 == null && s2 == null) {
			return true;
		}

		if (s1 != null && s2 == null || s1 == null && s2 != null) {
			return false;
		}

		return s1.equals(s2);
	}

	public String getStaatString() throws Exception{
		
		ASVWertelistenEintrag wl = ASVWlStore.getInstance().findEintrag("2118", staatSchluessel);
		
		if(wl == null){
			return "";
		}
		
		return wl.anzeigeform;
		
	}
	
	
}

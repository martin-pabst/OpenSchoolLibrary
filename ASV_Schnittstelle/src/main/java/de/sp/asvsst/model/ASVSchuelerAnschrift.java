package de.sp.asvsst.model;

import java.util.ArrayList;
import java.util.List;

import org.simpleframework.xml.Default;
import org.simpleframework.xml.DefaultType;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import de.sp.asvsst.ParseASVDataException;
import de.sp.asvsst.model.wertelisten.ASVAnschriftstyp;

@Root(name = "schueleranschrift")
@Default(DefaultType.FIELD)
public class ASVSchuelerAnschrift {

	public int anschrift_wessen;

	@Element(name = "anschriftstyp", required = false)
	public String anschriftstypSchluessel;

	public boolean auskunftsberechtigt;

	public boolean hauptansprechpartner;

	@Element(required = false)
	public ASVAnschrift anschrift;

	@Element(required = false)
	public ASVPerson person;

	@ElementList(required = false)
	public List<ASVKommunikation> kommunikationsdaten = new ArrayList<ASVKommunikation>();

	public ASVAnschriftstyp getAnschriftsTyp() throws ParseASVDataException {

		return ASVAnschriftstyp.findBySchluessel(anschriftstypSchluessel);

	}

}

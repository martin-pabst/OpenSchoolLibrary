package de.sp.asvsst.model;

import java.util.ArrayList;
import java.util.List;

import org.simpleframework.xml.Default;
import org.simpleframework.xml.DefaultType;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import de.sp.asvsst.ParseASVDataException;
import de.sp.asvsst.model.wertelisten.ASVKlassenart;

@Root(name = "klasse")
@Default(DefaultType.FIELD)
public class ASVKlasse {

	public int xml_id;

	public String klassenname;

	@Element(required = false)
	public String klassenname_lang;

	@Element(required = false)
	public String klassenname_naechstes_schuljahr;

	@Element(required = false)
	public String klassenname_zeugnis;

	@Element(name = "klassenart")
	public String klassenartSchluessel;

	@ElementList(name = "klassenleitungen")
	public List<ASVKlassenleitung> klassenleitungen = new ArrayList<>();

	@ElementList(name = "klassengruppen")
	public List<ASVKlassengruppe> klassengruppen = new ArrayList<>();

	public ASVKlassenart getKlassenart() throws ParseASVDataException {
		return ASVKlassenart.findBySchluessel(klassenartSchluessel);
	}

}

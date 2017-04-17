package de.sp.asvsst.model;

import java.util.ArrayList;
import java.util.List;

import org.simpleframework.xml.Default;
import org.simpleframework.xml.DefaultType;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import de.sp.asvsst.ParseASVDataException;
import de.sp.asvsst.model.wertelisten.ASVBildungsgang;

@Root(name = "klassengruppe")
@Default(DefaultType.FIELD)
public class ASVKlassengruppe {

	public int xml_id;

	@Element(name = "bildungsgang", required = false)
	public String bildungsgangSchluessel;

	@Element(name = "jahrgangsstufe", required = false)
	public String jahrgangsstufeSchluessel;

	public String kennung;

	@ElementList(name = "schuelerliste")
	public List<ASVSchuelerin> schueler = new ArrayList<>();

	public ASVBildungsgang getBildungsgang() throws ParseASVDataException {
		return ASVBildungsgang.findBySchluessel(bildungsgangSchluessel);
	}

}

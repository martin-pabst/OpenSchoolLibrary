package de.sp.asvsst.model;

import org.simpleframework.xml.Default;
import org.simpleframework.xml.DefaultType;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name="lehrkraftdaten_nicht_schulbezogen")
@Default(DefaultType.FIELD)
public class ASVLehrkraftSchuljahr {

	public int xml_id;
	
	public String lokales_differenzierungsmerkmal;
	
	public String familienname;
	
	@Element(required = false)
	public String vornamen;
	
	@Element(name = "geschlecht")
	public String geschlechtSchluessel;
	
	@Element(name="akademischer_grad", required = false)
	public String akademischerGradSchluessel;

	@Element(required = false)
	public String namensbestandteil_vorangestellt = "";
	
	@Element(required = false)
	public String namensbestandteil_nachgestellt = "";

	public String rufname;
	
	@Element(required = false)
	public String zeugnisname_1;
	
	@Element(required = false)
	public String zeugnisname_2;
	
	@Element(name = "amtsbezeichnung", required = false)
	public String amtsbezeichnungSchluessel;
	
	@Element(required = false)
	public ASVAnschrift anschrift;

	
	
}

package de.sp.asvsst.model;

import java.util.Date;

import org.simpleframework.xml.Default;
import org.simpleframework.xml.DefaultType;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "schullaufbahn")
@Default(DefaultType.FIELD)
public class ASVSchullaufbahn {

	@Element(required = false)
	public String schulverzeichnis_id;
	
	public Date datum;
	
	@Element(name="schuljahr", required = false)
	public String schuljahrSchluessel;

	@Element(name="jahrgangsstufe", required = false)
	public String jahrgangsstufeSchluessel;
	
	@Element(required = false)
	public int schulbesuchsjahr;
	
	@Element(name="bildungsgang", required = false)
	public String bildungsgangSchluessel;
	
	@Element(name="vorgang", required = false)
	public String vorgangSchluessel;
	
	@Element(name="vorgangZusatz", required = false)
	public String vorgangZusatzSchluessel;
	
	@Element(required = false)
	public String vorgangBemerkung;
	
	@Element(required = false)
	public String klassenname;
	
	@Element(required = false)
	public Boolean aktivierung;
	
	

}


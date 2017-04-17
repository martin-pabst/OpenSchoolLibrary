package de.sp.asvsst.model;

import org.simpleframework.xml.Default;
import org.simpleframework.xml.DefaultType;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "fach")
@Default(DefaultType.FIELD)
public class ASVFach {

	public int xml_id;
	
	public String kurzform;
	
	public String anzeigeform;
	
	public String langform;
	
	public boolean ist_selbst_erstellt;

	@Element(name = "asd_fach")
	public String asd_fach_schluessel;
	
	@Element(required = false)
	public String bezeichnung_zwischenzeugnis;
	
	@Element(required = false)
	public String bezeichnung_jahreszeugnis;

	@Element(required = false)
	public String bezeichnung_abschlusszeugnis;

	@Element(required = false)
	public String bezeichnung_abschlusszeugnis_2;
	
	@Element(required = false)
	public String bezeichnung_weitere_form;
	
	@Element(name = "besonderheit_zeugnis", required = false)
	public String besonderheit_zeugnis_schluessel;

	@Element(required = false)
	public String zusatzbezeichnung_zeugnis;

	
	
	
	
	
	
}


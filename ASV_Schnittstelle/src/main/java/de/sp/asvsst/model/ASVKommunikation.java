package de.sp.asvsst.model;

import org.simpleframework.xml.Default;
import org.simpleframework.xml.DefaultType;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import de.sp.asvsst.ParseASVDataException;
import de.sp.asvsst.model.wertelisten.ASVKommunikationsTyp;

@Root(name="kommunikation")
@Default(DefaultType.FIELD)
public class ASVKommunikation {
	
	@Element(name = "typ")
	public String typSchluessel;
	
	@Element(required = false)
	public String nummer_adresse;
	
	@Element(required = false)
	public String bemerkung;
	
	public ASVKommunikationsTyp getTyp() throws ParseASVDataException{
	
		return ASVKommunikationsTyp.findBySchluessel(typSchluessel);
		
	}
	
	
	
}

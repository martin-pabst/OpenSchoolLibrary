package de.sp.asvsst.model;

import org.simpleframework.xml.Default;
import org.simpleframework.xml.DefaultType;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "abweichung")
@Default(DefaultType.FIELD)
public class ASVAbweichung {

	@Element(required = false)
	public Double stunden;
	
	public boolean ist_kuerzung;
	
	@Element(name = "zusatzbedarfsgrund", required = false)
	public String zusatzbedarfsgrundSchluessel;
	
	@Element(name = "kuerzungsgrund", required = false)
	public String kuerzungsgrundSchluessel;

	
}

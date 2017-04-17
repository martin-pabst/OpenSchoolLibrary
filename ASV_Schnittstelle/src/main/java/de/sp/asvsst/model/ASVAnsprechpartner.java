package de.sp.asvsst.model;

import org.simpleframework.xml.Default;
import org.simpleframework.xml.DefaultType;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name="anschrift")
@Default(DefaultType.FIELD)
public class ASVAnsprechpartner {

	@Element(required = false)
	public String name;
	
	public ASVKommunikation kommunikation;
	
}

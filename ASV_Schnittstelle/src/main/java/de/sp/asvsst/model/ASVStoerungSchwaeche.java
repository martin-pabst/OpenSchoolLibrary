package de.sp.asvsst.model;

import java.util.Date;

import org.simpleframework.xml.Default;
import org.simpleframework.xml.DefaultType;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name="stoerungschwaeche")
@Default(DefaultType.FIELD)
public class ASVStoerungSchwaeche {

	@Element(name = "art")
	public String artSchluessel;
	
	@Element(required = false)
	public Date attest_bis;
	
	
	
	
}

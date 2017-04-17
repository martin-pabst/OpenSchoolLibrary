package de.sp.asvsst.model;

import org.simpleframework.xml.Default;
import org.simpleframework.xml.DefaultType;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "schulverzeichniseintrag")
@Default(DefaultType.FIELD)
public class ASVSchulverzeichniseintrag {

	public int xml_id;
	
	public String schulnummer;
	
	@Element(name = "schulart", required = false)
	public String schulartSchluessel;
	
	@Element(required = false)
	public String dienststellenname;
	
	
}

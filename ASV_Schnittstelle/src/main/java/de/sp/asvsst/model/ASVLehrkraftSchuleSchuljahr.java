package de.sp.asvsst.model;

import org.simpleframework.xml.Default;
import org.simpleframework.xml.DefaultType;
import org.simpleframework.xml.Root;

@Root(name="lehrkraft")
@Default(DefaultType.FIELD)
public class ASVLehrkraftSchuleSchuljahr {

	public int xml_id;
	
	public int lehrkraftdaten_nicht_schulbezogen_id;
	
	public String namenskuerzel;
	
	
}

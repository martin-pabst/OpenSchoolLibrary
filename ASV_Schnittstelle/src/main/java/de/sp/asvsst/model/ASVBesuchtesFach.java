package de.sp.asvsst.model;

import java.util.ArrayList;
import java.util.List;

import org.simpleframework.xml.Default;
import org.simpleframework.xml.DefaultType;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root(name = "besuchtes_fach")
@Default(DefaultType.FIELD)
public class ASVBesuchtesFach {

	@Element(name="fach_id", required = true)
	public String fach_id;

	@ElementList(required = false)
	public List<ASVUnterrichtselementID> unterrichtselemente = new ArrayList<>();
	
	@Element(required = false)
	public String unterrichtsart;
	
	@Element(name="belegungsart", required = false)
	public String belegungsartSchluessel;
	
}

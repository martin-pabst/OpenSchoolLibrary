package de.sp.tools.developer.asvwl2enum;

import java.util.List;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root(name="werteliste", strict = false)
public class ASVWerteliste {
	
	@Attribute
	public String bezeichnung;
	
	@Attribute
	public String pl_key;
	
	@ElementList(inline = true)
	public List<ASVWertelistenEintrag> eintraege;

}

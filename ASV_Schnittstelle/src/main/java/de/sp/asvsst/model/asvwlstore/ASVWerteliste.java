package de.sp.asvsst.model.asvwlstore;

import java.util.HashMap;
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

	private HashMap<String, ASVWertelistenEintrag> schluesselMap = null;
	
	public ASVWertelistenEintrag findBySchluessel(String schluessel){
		if(schluesselMap == null){
			buildSchluesselMap();
		}
		
		return schluesselMap.get(schluessel);
	}

	private void buildSchluesselMap() {
		
		schluesselMap = new HashMap<>();
		
		for(ASVWertelistenEintrag eintrag: eintraege){
			schluesselMap.put(eintrag.schluessel, eintrag);
		}
		
	}
	
	
}

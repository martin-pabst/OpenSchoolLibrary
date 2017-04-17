package de.sp.asvsst.model;

import java.util.ArrayList;
import java.util.List;

import org.simpleframework.xml.Default;
import org.simpleframework.xml.DefaultType;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root(name="schule", strict = false)
@Default(DefaultType.FIELD)
public class ASVSchule {
	
	public String schulnummer;
	public String schuljahr;
	public String schulart;
	@Element(required=false)
	public String amtliche_bezeichnung1;
	@Element(required=false)
	public String amtliche_bezeichnung2;
	@Element(required=false)
	public String kurzname;
	@Element(required=false)
	public String kurznameZusatz;
	@Element(required=false)
	public String dienststellenname;
	@Element(required=false)
	public String bezeichnung_genitiv;
	@Element(required=false)
	public String bezeichnung_akkusativ;
	@Element(required=false)
	public String bezeichnung_dativ;
	@Element(required=false)
	public String zusatzbezeichnung_zeugnis;
	@Element(required=false)
	public String schultraeger;
	@Element(required=false)
	public String Schulaufwandstraeger;

	@Element(required=false)
	public ASVAnschrift anschrift = new ASVAnschrift();
	
	@ElementList(name="klassen")
	public List<ASVKlasse> klassen = new ArrayList<>();
	
	@ElementList(name="lehrkraefte")
	public List<ASVLehrkraftSchuleSchuljahr> lehrkraefte = new ArrayList<>();

	@ElementList(name="faecher")
	public List<ASVFach> faecher = new ArrayList<>();
	
	public int getSchuelerAnzahl(){
		
		int anz = 0;
		
		for(ASVKlasse klasse: klassen){
			for(ASVKlassengruppe kg: klasse.klassengruppen){
				anz += kg.schueler.size();
			}
		}
		
		return anz;
		
	}
	
}

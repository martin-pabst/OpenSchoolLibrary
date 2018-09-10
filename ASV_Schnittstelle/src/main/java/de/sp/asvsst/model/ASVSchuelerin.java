package de.sp.asvsst.model;

import de.sp.asvsst.ParseASVDataException;
import de.sp.asvsst.model.wertelisten.ASVGeschlecht;
import org.simpleframework.xml.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Root(name="schuelerin")
@Default(DefaultType.FIELD)
public class ASVSchuelerin {

	public int xml_id;
	
	public String lokales_differenzierungsmerkmal;
	
	public String familienname;
	
	public String vornamen;
	
	public String rufname;
	
	@Element(required = false)
	public String namensbestandteil_vorangestellt = "";
	
	@Element(required = false)
	public String namensbestandteil_nachgestellt = "";

	@Element(name = "geschlecht")
	public String geschlechtSchluessel;
	
	@Element(name= "anrede", required = false)
	public String anredeSchluessel;
	
	@Element(name = "staatsangehoerigkeit")
	public String staatsangehoerigkeitSchluessel;
	
	@Element(name = "religionszugehoerigkeit", required = false)
	public String religionszugehoerigkeitSchluessel;
	
	public Date geburtsdatum;
	
	@Element(name="geburtsdatum_art")
	public String geburtsdatumArtSchluessel;
	
	public String geburtsort;
	
	@Element(name="geburtsland", required = false)
	public String geburtslandSchluessel;
	
	@Element(name="schueler_wohnt")
	public String schuelerWohntSchluessel;
	
	@Element(required = false)
	public Integer jahr_nachpruefung;
	
	@Element(required = false)
	public Date probezeit_bis;
	
	@Element(required = false)
	public String unterbringungSchluessel;

	public Date eintrittsdatum;
	
	@Element(name="eintritt_jahrgangsstufe", required = false)
	public String eintrittJahrgangsstufeSchluessel;
	
	@Element(required = false)
	public Date austrittsdatum;

	@Element(required = false)
	public int sortierung;

	@ElementList(name="schueleranschriften")
	public List<ASVSchuelerAnschrift> schueleranschriften = new ArrayList<>();
	
	@ElementList(name="ansprechpartnerliste", required = false)
	public List<ASVAnsprechpartner> ansprechpartner = new ArrayList<>();

	@ElementList(name="fremdsprachen", required = false)
	public List<ASVFremdsprache> fremdsprachen = new ArrayList<>();
	
	@ElementList(name="stoerungschwaecheliste", required = false)
	public List<ASVStoerungSchwaeche> stoerungSchwaecheListe = new ArrayList<>();
	
	@Element(name="ziel_jgst_ende_schuljahr", required = false)
	public String zielJgstEndeSchuljahr;
	
	@Element(name="ziel_jgst_vorjahr", required = false)
	public String zielJgstVorjahr;
	
	@Element(required = false)
	public Boolean notenausgleich_vorjahr;
	
	@Element(required = false)
	public Boolean notenausgleich;
	
	@Element(name="wiederholungsart", required = false)
	public String wiederholungartSchluessel;
	
	@Element(name="abweisung", required = false)
	public String abweisungSchluessel;
	
	@Element(name="gefaehrdung", required = false)
	public String gefaehrdungSchluessel;
	
	@Element(name="religion_ethik", required = false)
	public String religionEthikSchluessel;
	
	@Element(required = false)
	public Boolean sportbefreiung;
	
	@ElementList(name="besuchte_faecher", required = false)
	public List<ASVBesuchtesFach> besuchteFaecher = new ArrayList<>();

	@ElementList(name="schullaufbahnliste", required = false)
	public List<ASVSchullaufbahn> schullaufbahnliste = new ArrayList<>();

	public ASVGeschlecht getGeschlecht() throws ParseASVDataException {
		return ASVGeschlecht.findBySchluessel(geschlechtSchluessel);
	}
	
	

}

package de.sp.asvsst.model;

import org.simpleframework.xml.Default;
import org.simpleframework.xml.DefaultType;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import de.sp.asvsst.ParseASVDataException;
import de.sp.asvsst.model.wertelisten.ASVJahrgangsstufe;
import de.sp.asvsst.model.wertelisten.ASVUnterrichtsfach;

@Root(name = "fremdsprache")
@Default(DefaultType.FIELD)
public class ASVFremdsprache {

	@Element(name = "von_jahrgangsstufe", required = false)
	public String vonJahrgangsstufeSchluessel;

	@Element(name = "bis_jahrgangsstufe", required = false)
	public String bisJahrgangsstufeSchluessel;

	@Element(required = false)
	public Boolean feststellungspruefung;

	@Element(required = false)
	public String bemerkung;

	@Element(name = "niveaustufe", required = false)
	public String niveaustufeSchluessel;

	@Element(name = "unterrichtsfach", required = true)
	public String unterrichtsfachSchluessel;

	public ASVUnterrichtsfach getUnterrichtsfach() throws ParseASVDataException {

		return ASVUnterrichtsfach
				.findBySchluessel(unterrichtsfachSchluessel);

	}

	public ASVJahrgangsstufe getVonJahrgangsstufe() throws ParseASVDataException {
		return ASVJahrgangsstufe.findBySchluessel(vonJahrgangsstufeSchluessel);
	}

	public ASVJahrgangsstufe getBisJahrgangsstufe() throws ParseASVDataException {
		return ASVJahrgangsstufe.findBySchluessel(bisJahrgangsstufeSchluessel);
	}

	public Integer getVonJahrgangsstufeInt() throws ParseASVDataException {
		ASVJahrgangsstufe jgst = getVonJahrgangsstufe();
		if(jgst == null){
			return null;
		}
		return jgst.getSchulbesuchsjahr();
	}
	
	public Integer getBisJahrgangsstufeInt() throws ParseASVDataException {
		ASVJahrgangsstufe jgst = getBisJahrgangsstufe();
		if(jgst == null){
			return null;
		}
		return jgst.getSchulbesuchsjahr();
	}
	
}

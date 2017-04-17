package de.sp.asvsst.model;

import org.simpleframework.xml.Default;
import org.simpleframework.xml.DefaultType;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import de.sp.asvsst.ParseASVDataException;
import de.sp.asvsst.model.wertelisten.ASVKlassenleitungArt;

@Root(name="klasse")
@Default(DefaultType.FIELD)
public class ASVKlassenleitung {

	@Element(required = false)
	public String lehrkraft_id;
	
	@Element(name="klassenleitung_art")
	public String klassenleitung_art_schluessel;
	
	
	
	public ASVKlassenleitungArt getKlassenleitungArt() throws ParseASVDataException{
		
		return ASVKlassenleitungArt.findBySchluessel(klassenleitung_art_schluessel);
		
	}
	
	
}

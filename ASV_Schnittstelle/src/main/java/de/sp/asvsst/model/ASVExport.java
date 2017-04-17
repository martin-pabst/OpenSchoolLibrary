package de.sp.asvsst.model;

import java.util.ArrayList;
import java.util.List;

import org.simpleframework.xml.Default;
import org.simpleframework.xml.DefaultType;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root(name = "asv_export")
@Default(DefaultType.FIELD)
public class ASVExport {
	
	@ElementList(name="schulen", required = false)
	public List<ASVSchule> schulen;
	
	@ElementList(name="lehrkraftdaten_nicht_schulbezogen_liste", required = false)
	public List<ASVLehrkraftSchuljahr> lehrkraefte = new ArrayList<>();
	
	@ElementList(name="schulverzeichnis_liste", required = false)
	public List<ASVSchulverzeichniseintrag> schulverzeichnis = new ArrayList<>();
	
	
}

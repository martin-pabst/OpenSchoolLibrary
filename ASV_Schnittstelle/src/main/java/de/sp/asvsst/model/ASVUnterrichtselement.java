package de.sp.asvsst.model;

import java.util.Date;

import org.simpleframework.xml.Default;
import org.simpleframework.xml.DefaultType;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name="unterrichtselement")
@Default(DefaultType.FIELD)
public class ASVUnterrichtselement {

	public int xml_id;
	
	@Element(required = false)
	public int lehrkraft_id;
	
	@Element(required = false)
	public int klassengruppe_id;
	
	public int fach_id;
	
	@Element(required = false)
	public String fachspalte;
	
	@Element(required = false)
	public ASVAbweichung abweichung;
	
	@Element(required = false)
	public String bezeichnung;
	
	@Element(name = "unterrichtsart", required = false)
	public String unterrichtsartSchluessel;
	
	public boolean in_matrix;
	
	public Double stunden;
	
	@Element(required = false)
	public Double jahresstunden;
	
	@Element(required = false)
	public Boolean ist_wissenschaftlich;
	
	@Element(name = "bereich", required = false)
	public String bereichSchluessel;
	
	@Element(name = "unterrichtseigenschaft", required = false)
	public String unterrichtseigenschaftSchluessel;
	
	@Element(required = false)
	public String bemerkung;
	
	@Element(required = false)
	public Date von;
	
	@Element(required = false)
	public Date bis;

	@Element(name = "lehrbereich", required = false)
	public String lehrbereichSchluessel;
	
	public Boolean von_sammelzeile;
	
	@Element(required = false)
	public ASVKoppel koppel;
	
	
	
	
	
}

package de.sp.asvsst.model;

import org.simpleframework.xml.Root;
import org.simpleframework.xml.Text;

@Root(name = "unterrichtselement_id")
public class ASVUnterrichtselementID {

	@Text
	public String id;
	
}

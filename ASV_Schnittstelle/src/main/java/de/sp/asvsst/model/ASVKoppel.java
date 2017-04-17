package de.sp.asvsst.model;

import org.simpleframework.xml.Default;
import org.simpleframework.xml.DefaultType;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "koppel")
@Default(DefaultType.FIELD)
public class ASVKoppel {

	public String kurzform;

	@Element(required = false)
	public Boolean is_pseudokoppel;

}

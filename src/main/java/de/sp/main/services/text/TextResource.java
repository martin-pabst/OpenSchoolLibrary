package de.sp.main.services.text;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Text;

@Root(name="text")
public class TextResource {

	@Attribute
	public String path;
	
	@Text
	public String text;
	
}

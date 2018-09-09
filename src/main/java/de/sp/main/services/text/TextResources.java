package de.sp.main.services.text;

import java.util.ArrayList;
import java.util.List;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root(name = "textresources")
public class TextResources {

	@Attribute
	public String path;

	@Attribute
	public String language;

	@ElementList(inline = true)
	public List<TextResource> resources = new ArrayList<>();

}

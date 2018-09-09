package de.sp.main.services.text;

import java.util.ArrayList;
import java.util.List;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root(name="textresourcefile")
public class TextResourceFile {

	@ElementList(inline = true)
	public List<TextResources> textResources = new ArrayList<>();
	
}

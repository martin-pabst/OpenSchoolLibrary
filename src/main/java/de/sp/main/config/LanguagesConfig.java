package de.sp.main.config;

import java.util.ArrayList;
import java.util.List;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root(name = "languages")
public class LanguagesConfig {

	@Attribute(name="default")
	public String defaultCode;
	
	@ElementList(name = "languages", inline = true)
	public List<LanguageConfig> languages = new ArrayList<>();

	
}

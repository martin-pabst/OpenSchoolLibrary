package de.sp.main.config;

import java.util.ArrayList;
import java.util.List;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Default;
import org.simpleframework.xml.DefaultType;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Transient;

import de.sp.main.resources.text.TextMap;

@Root(name="language")
@Default(DefaultType.FIELD)
public class LanguageConfig {
	
	@Attribute(required = false)
	public boolean load = true;
	
	public String languageCode; // e.g. "de-DE", "de-CH", "en-en", ...
	
	public String name; // name of language in language itself
	
	public String englishName; // name of language in English
	
	public String country;
	
	public String englishCountryName; // name of country in English
	
	@ElementList(inline = true)
	public List<LanguageCode> fallbackCodes = new ArrayList<LanguageCode>(); // e.g. "de-DE", ...
	
	@Transient
	public TextMap textMap = new TextMap();
	
}

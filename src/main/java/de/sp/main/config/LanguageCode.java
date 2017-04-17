package de.sp.main.config;

import org.simpleframework.xml.Root;
import org.simpleframework.xml.Text;

@Root(name = "fallback")
public class LanguageCode {
	@Text
	public String languageCode;
}

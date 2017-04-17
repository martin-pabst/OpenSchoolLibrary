package de.sp.main.resources.modules;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name="moduleinfo")
public class ModuleInfo {
	
	@Element
	private String name;
	
	@Element
	private String mainclass;

	public String getName() {
		return name;
	}

	public String getMainclass() {
		return mainclass;
	}
	
	
}

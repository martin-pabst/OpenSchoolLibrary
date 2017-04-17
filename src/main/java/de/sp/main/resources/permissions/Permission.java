package de.sp.main.resources.permissions;

import de.sp.main.resources.modules.Module;

public interface Permission {
	
	public String getIdentifier();
	
	public String getDescription();
	
	public Module getModule();
	
}

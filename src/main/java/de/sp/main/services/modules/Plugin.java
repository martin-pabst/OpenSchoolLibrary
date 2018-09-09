package de.sp.main.services.modules;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import de.sp.database.model.User;
import de.sp.main.services.text.TS;

public abstract class Plugin extends Module {
	
	private Path webContentDir = null;

	private List<Path> jarFiles = new ArrayList<>();
	
	@Override
	public abstract String getIdentifier();
	
	@Override
	public Path getWebContentDir() {
		return webContentDir;
	}

	public void setWebContentDir(Path webContentDir) {
		this.webContentDir = webContentDir;
	}

	public void setJarFiles(List<Path> jarFiles) {
		
		this.jarFiles = jarFiles;
		
	}

	public List<Path> getJarFiles() {
		return jarFiles;
	}
	
	@Override
	abstract public void getHtmlFragment(String fragmentId, TS ts, User user, Long school_id, StringBuilder sb);
	
	@Override
	abstract public String[] addFragmentIds();
	
	
	
	
}

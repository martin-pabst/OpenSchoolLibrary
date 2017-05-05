package de.sp.main.resources.modules;

import de.sp.database.model.User;
import de.sp.main.mainframe.menu.MenuItem;
import de.sp.main.resources.text.TS;
import de.sp.modules.admin.AdminModule;
import de.sp.modules.calendar.CalendarModule;
import de.sp.modules.install.InstallModule;
import de.sp.modules.library.LibraryModule;
import de.sp.modules.mail.MailModule;
import de.sp.modules.user.UserModule;
import de.sp.tools.file.FileTool;
import de.sp.tools.file.JarFileIncluder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.lang.reflect.Constructor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class ModuleManager {

	private static ArrayList<Module> modules = new ArrayList<>();

	private static HashMap<String, Module> moduleMap = new HashMap<>();

	private static HashMap<String, Module> htmlFragmentIdMap = new HashMap<>();

	private static ArrayList<Permission> permissions = null;


	static {

		registerBuiltInModules();

	}

	public static void addModule(Module module) {
		modules.add(module);
		moduleMap.put(module.getName(), module);

		for (String fragmentId : module.addFragmentIds()) {
			htmlFragmentIdMap.put(fragmentId, module);
		}

		Logger logger = LoggerFactory.getLogger(ModuleManager.class);
		logger.info("Adding module " + module.getIdentifier());

	}

	private static void registerBuiltInModules() {
		addModule(new UserModule());
		addModule(new MailModule());
		addModule(new CalendarModule());
		addModule(new LibraryModule());
		addModule(new AdminModule());
		addModule(new InstallModule());
	}

	public static Module getModule(String moduleName) {
		return moduleMap.get(moduleName);
	}

	public static List<Module> getModules() {
		return modules;
	}

	public static void registerPlugins() {

		Logger logger = LoggerFactory.getLogger(ModuleManager.class);

		logger.info("Adding plugins ...");

		Path path = Paths.get("plugins");
		Path developmentPath = Paths.get("target/deploy/plugins");

		try {

			List<Path> subDirectories = FileTool.listFilesAndDirectories(path,
					true, false, false, null);

			subDirectories.addAll(FileTool.listFilesAndDirectories(developmentPath,
					true, false, false, null));

			for (Path p : subDirectories) {
				registerPlugin(p);
			}

		} catch (Exception e) {
			logger = LoggerFactory.getLogger(ModuleManager.class);
			logger.error("ModuleManager: ", e);
		}

		logger.info("... adding plugins done!");

	}

	private static void registerPlugin(Path p) throws Exception {

		Logger logger = LoggerFactory.getLogger(ModuleManager.class);

		Path jarDir = Paths.get(p.toString(), "jars");

		if (!Files.exists(jarDir)) {

			logger.error("Trying to examine external module. Directory "
					+ jarDir.toString() + " doesn't exist.");
			return;

		}

		List<Path> jarFiles = FileTool.listFilesAndDirectories(jarDir, false,
				true, true, ".jar");

		Path webContentPath = Paths.get(p.toAbsolutePath().toString(),
				"WebContent");
		if (!Files.exists(webContentPath)) {
			webContentPath = null;
		}

		File module_info_XMLFile = Paths.get(p.toAbsolutePath().toString(),
				"module_info.xml").toFile();

		if (!module_info_XMLFile.exists()) {
			logger.error("Trying to examine external module. File "
					+ module_info_XMLFile.toString() + " doesn't exist.");
		}

		Serializer serializer = new Persister();

		ModuleInfo moduleInfo = serializer.read(ModuleInfo.class,
				module_info_XMLFile);

		for (Path path : jarFiles) {
			JarFileIncluder.addJar(path.toFile());
		}

		Class<?> clazz = Class.forName(moduleInfo.getMainclass());
		Constructor<?> ctor = clazz.getConstructor();
		Plugin module = (Plugin) ctor.newInstance();

		module.setJarFiles(jarFiles);

		module.setWebContentDir(webContentPath);

		addModule(module);

	}

	public static List<Plugin> getPlugins() {

		ArrayList<Plugin> externalModules = new ArrayList<>();

		modules.forEach(module -> {
			if (module instanceof Plugin) {
				externalModules.add((Plugin) module);
			}
		});

		return externalModules;
	}

	public static List<Path> getAllExternalJarfiles() {

		ArrayList<Path> jarFiles = new ArrayList<>();

		for (Module m : modules) {
			if (m instanceof Plugin) {
				jarFiles.addAll(((Plugin) m).getJarFiles());
			}
		}

		return jarFiles;

	}

	public static void getMenuHtml(StringBuilder sbLeft, StringBuilder sbRight,
			User user, int indentationLevel, TS ts, Long school_id) {

		ArrayList<MenuItem> menuItems = new ArrayList<>();
		HashMap<String, MenuItem> idMap = new HashMap<>();

		/*
		 * first round: insert each menuitems which are not positioned
		 * relatively to other menuitems
		 */
		modules.forEach(module -> {
			if (module.isActivated()) {
				for (MenuItem mi : module.getMenuItems()) {
					if (mi.getInsertBelowThisId().isEmpty()) {
						menuItems.add(mi);
						if (!mi.getId().isEmpty()) {
							idMap.put(mi.getId(), mi);
						}
					}
				}
			}
		});

		/*
		 * first round: insert each menuitems which are positioned relatively to
		 * other menuitems
		 */
		modules.forEach(module -> {
			if (module.isActivated()) {
				for (MenuItem mi : module.getMenuItems()) {
					if (!mi.getInsertBelowThisId().isEmpty()) {

						MenuItem parent = idMap.get(mi.getInsertBelowThisId());
						if (parent != null) {
							parent.addItem(mi);
						} else {
							menuItems.add(mi);
						}

					}
				}
			}
		});

		Collections.sort(menuItems);

		menuItems.forEach(mi -> {
			switch (mi.getSide()) {
			case left:
				mi.addHtml(sbLeft, indentationLevel, user, ts, school_id);
				break;
			case right:
				mi.addHtml(sbRight, indentationLevel, user, ts, school_id);
				break;
			}
		});

	}

	public static String getJSImportStatements(String indentation) {

		StringBuilder sb = new StringBuilder();

		modules.forEach(module -> {
			if (module.isActivated()) {
				module.getJSImportStatements(indentation, sb);
			}
		});

		return sb.toString();
	}

	public static String getCSSImportStatements(String indentation) {

		StringBuilder sb = new StringBuilder();

		modules.forEach(module -> {
			if (module.isActivated()) {
				module.getCSSImportStatements(indentation, sb);
			}
		});

		return sb.toString();
	}

	public static String getHtmlFragment(String fragmentId, TS ts, User user,
			Long school_id) throws InsufficientPermissionException {

		StringBuilder sb = new StringBuilder();

		Module m = htmlFragmentIdMap.get(fragmentId);

		if (m == null) {
			Logger logger = LoggerFactory.getLogger(ModuleManager.class);
			logger.error("No Module found for htmlFragment with id "
					+ fragmentId);
		} else {

			user.checkPermission(m.getIdentifier() + ".open", school_id);

			m.getHtmlFragment(fragmentId, ts, user, sb);

		}

		return sb.toString();

	}

	public static List<Permission> getAllPermissions() {

		if(permissions == null) {

			permissions = new ArrayList<>();

			modules.forEach(module -> {

				for (Permission p : module.getPermissions()) {
					permissions.add(p);
				}

			});
		}

		return permissions;
	}

	public static void addServlets(ServletContextHandler context) {

		for (Module module : modules) {
			if (module.isActivated()) {
				module.addServlets(context);
			}
		}

	}

}

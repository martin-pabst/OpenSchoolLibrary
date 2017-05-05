package de.sp.main.resources.modules;

import de.sp.database.model.User;
import de.sp.main.mainframe.menu.MenuItem;
import de.sp.main.resources.text.TS;
import de.sp.tools.file.FileTool;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public abstract class Module {

	public abstract String getIdentifier();

	public abstract String getName();

	public Permission[] getPermissions() {

		String[] permissionNames = getPermissionNames();

		Permission[] permissions = new Permission[permissionNames.length];

		String moduleIdentifier = getIdentifier();

		for (int i = 0; i < permissionNames.length; i++) {

			String permissionName = permissionNames[i];

			String remarkIdentifier = permissionName;

			
			remarkIdentifier = "permissions." + remarkIdentifier;

			permissions[i] = new Permission(permissionName, remarkIdentifier);

		}

		return permissions;

	}

	public abstract String[] getPermissionNames();

	public abstract MenuItem[] getMenuItems();

	private boolean mayGetDeactivated = true;

	private boolean activated = true;

	private boolean installed = false;

	public boolean isMayGetDeactivated() {
		return mayGetDeactivated;
	}

	public void setMayGetDeactivated(boolean mayGetDeactivated) {
		this.mayGetDeactivated = mayGetDeactivated;
	}

	public boolean isActivated() {
		return activated;
	}

	public void setActivated(boolean activated) {
		this.activated = activated;
	}

	public boolean isInstalled() {
		return installed;
	}

	public void setInstalled(boolean installed) {
		this.installed = installed;
	}

	public Path getWebContentDir() {
		return Paths.get("WebContent/modules/" + getIdentifier());
	}

	public ArrayList<String> getJavascriptOrCSSFiles(boolean asRelativeURL,
			String suffix) {

		if (suffix.startsWith(".")) {
			suffix = suffix.substring(1);
		}

		Path path = Paths.get(getWebContentDir().toString(), suffix);
		String relativeURL = "/modules/" + getIdentifier() + "/" + suffix;

		ArrayList<String> files = new ArrayList<String>();

		try {

			if (Files.exists(path)) {

				List<Path> jsFiles = FileTool.listFiles(path, "." + suffix);

				jsFiles.forEach(p -> {

					String filename = p.toString();

					if (asRelativeURL) {
						filename = filename.substring(path.toString().length());
						filename = filename.replace('\\', '/');
						filename = relativeURL + filename;
					}

					files.add(filename);

				});

			}

		} catch (IOException e) {

			Logger logger = LoggerFactory.getLogger(this.getClass());

			logger.error("Error reading List of javascript files of module "
					+ getIdentifier(), e);
		}

		return files;

	}

	public void getJSImportStatements(String indentation, StringBuilder sb) {

		getJavascriptOrCSSFiles(true, "js").forEach(filename -> {

			sb.append(indentation);
			sb.append("<script type=\"text/javascript\" src=\"");
			sb.append(filename).append("\"></script>\n");

		});

	}

	public void getCSSImportStatements(String indentation, StringBuilder sb) {

		getJavascriptOrCSSFiles(true, "css").forEach(filename -> {

			// <link rel="stylesheet" href="/mainframe/main.css">

				sb.append(indentation);
				sb.append("<link rel=\"stylesheet\" href=\"");
				sb.append(filename).append("\">\n");

			});

	}

	public String getMinimumPermission() {

		return null;

	}

	protected void renderTemplate(Template template, TS ts, User user,
			StringBuilder sb) {



		VelocityContext context = new VelocityContext();
		context.put("ts", ts); // language
		context.put("user", user);

		/* now render the template into a StringWriter */
		StringWriter writer = new StringWriter();
		template.merge(context, writer);

		sb.append(writer);

	}

	public void addServlets(ServletContextHandler context) {
	};

	abstract public void getHtmlFragment(String fragmentId, TS ts, User user,
			StringBuilder sb);

	abstract public String[] addFragmentIds();

}

package de.sp.main.resources.templates;

import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

public class VelocityEngineFactory {

	private static VelocityEngine ve;

	public static VelocityEngine getVelocityEngine() {
		if (ve == null) {

			ve = new VelocityEngine();
			ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
			ve.setProperty("classpath.resource.loader.class",
					ClasspathResourceLoader.class.getName());

			ve.setProperty(Velocity.ENCODING_DEFAULT, "UTF-8");
			ve.setProperty(Velocity.INPUT_ENCODING, "UTF-8");
			ve.setProperty(Velocity.OUTPUT_ENCODING, "UTF-8");

			ve.init();

		}
		return ve;

	}

}

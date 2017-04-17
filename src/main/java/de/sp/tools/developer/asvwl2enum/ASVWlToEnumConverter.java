package de.sp.tools.developer.asvwl2enum;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashSet;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

public class ASVWlToEnumConverter {

	public static void main(String[] args) throws Exception {

		File file = new File(
				"C:/Users/martin/Downloads/notenverwaltung-definition_1.00/notenverwaltung_wertelisten_1.00/Akademische_Grade_(2005).xml");

		InputStream is = new FileInputStream(file);

		Serializer serializer = new Persister();

		ASVWerteliste werteliste = serializer.read(ASVWerteliste.class, is,
				false);

		StringBuilder sb = new StringBuilder();

		sb.append("public enum " + werteliste.bezeichnung + "{\n");

		// Doppel entfernen
		HashSet<String> schluessel = new HashSet<>();

		for (int i = 0; i < werteliste.eintraege.size(); i++) {

			ASVWertelistenEintrag eintrag = werteliste.eintraege.get(i);

			if (eintrag.gueltig_bis == null
					&& !schluessel.contains(eintrag.schluessel)) {
				schluessel.add(eintrag.schluessel);
//				if (eintrag.kurzform.startsWith("GY")
//						|| eintrag.kurzform.startsWith("RS")) {
					sb.append(eintrag.toString());
					if (i < werteliste.eintraege.size() - 1) {
						sb.append(", // \n");
					} else {
						sb.append("; // \n\n");
					}
//				}
			}
		}

		sb.append("private String schluessel;\n");
		sb.append("private String kurzform;\n");
		sb.append("private String anzeigeform;\n");
		sb.append("private String langform;\n\n");

		sb.append("}");

		System.out.println(sb.toString());

	}

}

package de.sp.asvsst.model.asvwlstore;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import org.sql2o.Connection;

import de.sp.database.daos.basic.ValueDAO;
import de.sp.database.model.Value;
import de.sp.database.valuelists.ValueListType;
import de.sp.tools.file.FileTool;

public class ASVWlStore {

	private static ASVWlStore instance;

	// Mappt PL-Keys auf Wertelisten
	private HashMap<String, ASVWerteliste> wlMap = new HashMap<>();

	// Mappt PL-Keys auf Dateinamen
	private HashMap<String, String> filenameMap = null;

	private ASVWlStore() {

	};

	public static ASVWlStore getInstance() {

		if (instance == null) {
			instance = new ASVWlStore();
		}

		return instance;
	}

	public static void releaseMemory() {
		instance = null;
	}

	public void init() throws URISyntaxException, IOException {

		filenameMap = new HashMap<>();

		List<String> files = FileTool.listAllResourceFiles("/asvwertelisten");

		for (String filename : files) {
			int von = filename.lastIndexOf("(");
			int bis = filename.lastIndexOf(")");
			if (von >= 0 && bis >= 0 && von + 1 < bis) {
				String pl_key = filename.substring(von + 1, bis);
				filenameMap.put(pl_key, filename);
			}
		}

	}

	public ASVWerteliste getWerteliste(String pl_key) throws Exception {

		ASVWerteliste wl = wlMap.get(pl_key);

		if (wl == null) {
			wl = loadWerteliste(pl_key);
		}

		if (wl != null) {
			return wl;
		}

		throw new Exception("ASV-Werteliste mit PL-Key " + pl_key
				+ " nicht gefunden.");

	}

	private ASVWerteliste loadWerteliste(String pl_key) throws Exception {

		if (filenameMap == null) {
			init();
		}

		String filename = filenameMap.get(pl_key);

		if (filename == null) {
			throw new Exception("Kann Werteliste zum PL-Key " + pl_key
					+ " nicht finden.");
		}

		String text = FileTool.readFile(filename);

		Serializer serializer = new Persister();

		ASVWerteliste werteliste = serializer.read(ASVWerteliste.class, text,
				false);

		return werteliste;

	}

	public ASVWertelistenEintrag findEintrag(String pl_key, String schluessel)
			throws Exception {

		ASVWerteliste wl = getWerteliste(pl_key);

		if (wl == null) {
			return null;
		}

		return wl.findBySchluessel(schluessel);

	}

	public Value findOrMakeValue(String pl_key, String asv_schluessel,
								 ValueListType valueStore, Long school_id, Connection con)
			throws Exception {

		if (asv_schluessel == null) {
			return null;
		}

		ASVWertelistenEintrag asvWlEintrag = findEintrag(pl_key, asv_schluessel);

		if (asvWlEintrag == null) {
			return null;
		}

		return ValueDAO.findOrWrite(school_id, valueStore.getKey(),
				asv_schluessel, con, asvWlEintrag.anzeigeform,
				asvWlEintrag.kurzform, 100);

	}

}

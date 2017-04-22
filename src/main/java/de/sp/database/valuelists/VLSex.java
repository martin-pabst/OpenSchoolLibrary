package de.sp.database.valuelists;

import de.sp.main.mainframe.definitionsservlet.SimpleValueListEntry;

import java.util.ArrayList;
import java.util.List;

public enum VLSex {
	male(1, "m", "männlich"), female(2, "w", "weiblich");

	private int key;
	private String shortName;
	private String longName;

	VLSex(int key, String shortName, String longName) {
		this.key = key;
		this.shortName = shortName;
		this.longName = longName;
	}

	public static VLSex findByKey(Integer key) throws ValueNotFoundException {

		if (key != null) {
			for (VLSex vls : VLSex.values()) {
				if (vls.key == key) {
					return vls;
				}
			}
		}

		throw new ValueNotFoundException("Value for key " + key
				+ " not found in valuelist sex.");
	}

	public int getKey() {
		return key;
	}

	public static List<SimpleValueListEntry> getAsValueList(){

		ArrayList<SimpleValueListEntry> values = new ArrayList<>();

		for(VLSex sex: VLSex.values()){
			values.add(new SimpleValueListEntry((long)sex.key, sex.shortName));
		}

		return values;
	}


}

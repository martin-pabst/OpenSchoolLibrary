package de.sp.database.valuelists;

public enum VLSex {
	male(1), female(2);

	private int key;

	private VLSex(int key) {
		this.key = key;
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
}

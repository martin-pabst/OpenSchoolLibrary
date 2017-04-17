package de.sp.database.valuelists;

public enum VLPersonType {
	mother(1), father(2), other(4);

	private int key;

	private VLPersonType(int key) {
		this.key = key;
	}

	public static VLPersonType findByKey(Integer key)
			throws ValueNotFoundException {

		if (key != null) {
			for (VLPersonType vls : VLPersonType.values()) {
				if (vls.key == key) {
					return vls;
				}
			}
		}

		throw new ValueNotFoundException("Value for key " + key
				+ " not found in valuelist person type.");
	}

	public int getKey() {
		return key;
	}
}

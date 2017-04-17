package de.sp.database.testdata;

import de.sp.database.setup.DatabaseSetup;

public class DatabaseSetupTest {

	public void testSetupDatabase() {

		DatabaseSetup dbSetup = new DatabaseSetup();

		dbSetup.setup(true, null);
		
	}

}

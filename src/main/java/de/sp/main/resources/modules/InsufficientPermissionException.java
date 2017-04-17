package de.sp.main.resources.modules;

import de.sp.database.model.School;
import de.sp.database.model.User;

public class InsufficientPermissionException extends Exception {

	public InsufficientPermissionException(User user, String permission,
			School school) {

		super("Isufficient permission: User " + user.getName()
				+ "(Id " + user.getId() + ") lacks permmission " + permission
				+ " for school " + school.getAbbreviation() + " (Id "
				+ school.getId() + ")");

	}

}

package de.sp.main.resources.modules;

import de.sp.database.model.School;
import de.sp.database.model.User;

public class InsufficientPermissionException extends Exception {

	public InsufficientPermissionException(String message){
		super(message);
	}

	public InsufficientPermissionException(User user, String permission,
			School school) {

		super(school == null ? "Insufficient permission: User " + user.getName() + " is not root." :
				"Isufficient permission: User " + user.getName()
						+ "(Id " + user.getId() + ") lacks permmission " + permission
						+ " for school " + school.getAbbreviation() + " (Id "
						+ school.getId() + ")");

	}

}

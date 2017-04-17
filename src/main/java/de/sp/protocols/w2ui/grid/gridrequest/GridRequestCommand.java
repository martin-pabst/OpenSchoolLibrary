package de.sp.protocols.w2ui.grid.gridrequest;


public enum GridRequestCommand {
	getRecords("get-records"), deleteRecords("delete-records"), saveRecords(
			"save-record"), update("update");

	private String commandText;

	private GridRequestCommand(String commandText) {
		this.commandText = commandText;
	}

	public static GridRequestCommand findByCommandText(String commandText) {
		for (GridRequestCommand grc : GridRequestCommand.values()) {
			if (grc.commandText.equals(commandText)) {
				return grc;
			}
		}
		return null;
	}

}

package de.sp.tools.server.progressServlet;

import java.util.Calendar;

public class ProgressResponseData {

	private int min;
	private int max;
	private int now;
	private String text;
	private boolean completed = false;
	private Object result;

	private transient long timestamp;

	public ProgressResponseData(int min, int max, int now, String text,
			boolean completed, Object result) {
		super();
		this.min = min;
		this.max = max;
		this.now = now;
		this.text = text;
		this.completed = completed;
		this.result = result;

		timestamp = Calendar.getInstance().getTimeInMillis();

	}

	/**
	 * We discard response data that is older than 2 hours. Call this to see if
	 * this data can be deleted.
	 * 
	 * @return
	 */
	public boolean isOutdated() {

		long now = Calendar.getInstance().getTimeInMillis();

		return now - timestamp > 2 * 60 * 60 * 1000;

	}

	public boolean isCompleted() {
		return completed;
	}

	public int getMin() {
		return min;
	}

	public int getMax() {
		return max;
	}

	public int getNow() {
		return now;
	}

	public String getText() {
		return text;
	}

	public Object getResult() {
		return result;
	}

	public long getTimestamp() {
		return timestamp;
	}

}

package dev.sdb.client;

import com.google.gwt.user.client.History;

public class HistoryManager {

	private static boolean LOG_URLS_AND_TOKENS = false;

	/**
	 * This token represents the current state of the application. Its value is <i>not</i> url encoded.
	 */
	private String token;

	public HistoryManager() {
		super();
	}

	public void setHistory(String token, boolean issueEvent) {
		if (LOG_URLS_AND_TOKENS) {
			System.out.println("Adding to history" + (!issueEvent ? " (without events)" : "") + ": " + token);
		}

		History.newItem(token, issueEvent);
		if (!issueEvent)
			setToken(token);
	}

	public String getToken() {
		return this.token;
	}

	public void setToken(String token) {
		if (LOG_URLS_AND_TOKENS) {
			System.out.println("Setting token to: " + token);
		}

		this.token = token;
	}
}

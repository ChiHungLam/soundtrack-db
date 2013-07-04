package dev.sdb.client.controller;

public enum ControllerType {
	HOME("home"),
	RELEASE("release"),
	MUSIC("music"),
	SOUNDTRACK("soundtrack"),
	SERIES("series");

	private String token = "";

	private ControllerType(String token) {
		if (token.contains("?"))
			throw new IllegalArgumentException("token must not contain the question mark");
		this.token = token.toLowerCase();
	}

	public String getToken() {
		return this.token;
	}

	public static ControllerType getByToken(String token) {
		if (token == null || token.isEmpty())
			return null;

		token = token.toLowerCase();

		for (ControllerType controllerType : values()) {
			if (controllerType.getToken().startsWith(token))
				return controllerType;
		}
		return null;
	}

	@Override public String toString() {
		return getToken();
	}
}

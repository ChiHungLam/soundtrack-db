package dev.sdb.client.presenter;

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
		if (token == null) {
			System.err.println("ControllerType.getByToken() - null token");
			return null;
		}
		if (token.isEmpty()) {
			return null;
		}
		token = token.toLowerCase();

		for (ControllerType controllerType : values()) {
			if (token.startsWith(controllerType.getToken()))
				return controllerType;
		}
		System.err.println("ControllerType.getByToken() - token not recognized: " + token);
		return null;
	}

	@Override public String toString() {
		return getToken();
	}
}

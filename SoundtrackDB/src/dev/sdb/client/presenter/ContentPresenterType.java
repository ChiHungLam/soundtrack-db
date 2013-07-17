package dev.sdb.client.presenter;

public enum ContentPresenterType {
	HOME("home", ""),
	RELEASE("release", "VÖ"),
	MUSIC("music", "Musik"),
	SOUNDTRACK("soundtrack", "Soundtrack"),
	SERIES("series", "Serien");

	private String token;
	private String name;

	private ContentPresenterType(String token, String name) {
		if (token.contains("?"))
			throw new IllegalArgumentException("token must not contain the question mark");
		this.token = token.toLowerCase();
		this.name = name;
	}

	public String getToken() {
		return this.token;
	}

	public String getName() {
		return this.name;
	}

	public static ContentPresenterType getByToken(String token) {
		if (token == null) {
			System.err.println("ControllerType.getByToken() - null token");
			return null;
		}
		if (token.isEmpty()) {
			return null;
		}
		token = token.toLowerCase();

		for (ContentPresenterType controllerType : values()) {
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

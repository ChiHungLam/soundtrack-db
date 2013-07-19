package dev.sdb.client.presenter;

public enum ContentPresenterType {
	HOME("home", "", "Home"),
	RELEASE("release", "VÖ", "Veröffentlichungen"),
	MUSIC("music", "Musik", "Musik"),
	SOUNDTRACK("soundtrack", "Soundtrack", "Soundtracks"),
	SERIES("series", "Serien", "Serien");

	private String token;
	private String name;
	private String sectionTitle;

	private ContentPresenterType(String token, String name, String sectionTitle) {
		if (token.contains("?"))
			throw new IllegalArgumentException("token must not contain the question mark");
		this.token = token.toLowerCase();
		this.name = name;
		this.sectionTitle = sectionTitle;
	}

	public String getToken() {
		return this.token;
	}

	public String getName() {
		return this.name;
	}

	public String getSectionTitle() {
		return this.sectionTitle;
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

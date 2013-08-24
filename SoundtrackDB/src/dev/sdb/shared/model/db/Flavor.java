package dev.sdb.shared.model.db;

import com.google.gwt.user.client.rpc.IsSerializable;

public enum Flavor implements IsSerializable {
	RELEASE("Veröffentlichung", "Veröffentlichungen", "Veröffentlichungs"),
	MUSIC("Musik", "Musiken", "Musik"),
	SOUNDTRACK("Soundtrack", "Soundtracks", "Soundtrack"),
	SERIES("Serie", "Serien", "Serien"),
	CATALOG("Katalog", "Kataloge", "Katalog"),
	GENRE("Genre", "Genres", "Genre");

	private final String singular;
	private final String plural;
	private final String prefix;

	Flavor(String singular, String plural, String prefix) {
		this.singular = singular;
		this.plural = plural;
		this.prefix = prefix;
	}

	public String getSingular() {
		return this.singular;
	}

	public String getPlural() {
		return this.plural;
	}

	public String getPrefix() {
		return this.prefix;
	}
}

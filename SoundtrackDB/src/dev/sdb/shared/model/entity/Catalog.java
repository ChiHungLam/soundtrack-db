package dev.sdb.shared.model.entity;

public class Catalog extends AbstractEntity {

	private String title;

	public Catalog() {
		super();
	}

	public Catalog(long id, String title) {
		super(id);
		this.title = title;
	}

	public String getTitle() {
		return this.title;
	}

	@Override public String getMatch() {
		return getTitle();
	}

	@Override public String toString() {
		return "Katalog #" + getId();
	}

}

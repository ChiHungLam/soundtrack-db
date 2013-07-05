package dev.sdb.shared.model.entity;

public class Release extends AbstractEntity {

	private String title;

	public Release() {
		super();
	}

	public Release(long id, String title) {
		super(id);
		this.title = title;
	}

	public String getTitle() {
		return this.title;
	}

	@Override public String toString() {
		String info = "Release #" + getId() + ": " + getTitle();
		return info;
	}
}

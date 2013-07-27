package dev.sdb.shared.model.entity;


public class Catalog extends AbstractEntity<Catalog> {

	private String title;
	private long parentId;
	private boolean children;

	public Catalog() {
		super();
	}

	public Catalog(long id, String title, long parentId, boolean children) {
		super(id);
		this.title = title;
		this.parentId = parentId;
		this.children = children;
	}

	public boolean hasChildren() {
		return this.children;
	}

	public String getTitle() {
		return this.title;
	}

	@Override public String getMatch() {
		return getTitle();
	}

	public long getParentId() {
		return this.parentId;
	}

	@Override public String toString() {
		return "Katalog #" + getId();
	}

}

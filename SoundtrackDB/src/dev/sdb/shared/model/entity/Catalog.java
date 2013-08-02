package dev.sdb.shared.model.entity;


public class Catalog extends AbstractEntity<Catalog> {

	private long parentId;
	private boolean children;
	private String title;
	private String info;
	private int eraBegin;
	private int eraEnd;

	public Catalog() {
		super();
	}

	public Catalog(long id, long parentId, boolean children, String title, String info, int eraBegin, int eraEnd) {
		super(id);
		this.parentId = parentId;
		this.children = children;
		this.title = title;
		this.info = info;
		this.eraBegin = eraBegin;
		this.eraEnd = eraEnd;
	}

	public boolean hasChildren() {
		return this.children;
	}

	public String getTitle() {
		return this.title;
	}

	public String getInfo() {
		return this.info;
	}

	public int getEraBegin() {
		return this.eraBegin;
	}

	public int getEraEnd() {
		return this.eraEnd;
	}

	public String getEra() {
		if (this.eraBegin > 0 && this.eraEnd > 0)
			return this.eraBegin + " - " + this.eraEnd;

		if (this.eraBegin > 0)
			return "Ab " + this.eraBegin;

		if (this.eraEnd > 0)
			return "Bis " + this.eraEnd;

		return null;
	}

	@Override public String getMatch() {
		String match = getTitle();
		if ((this.info != null) && !this.info.isEmpty())
			match += " [" + this.info + "]";
		return match;
	}

	public long getParentId() {
		return this.parentId;
	}

	@Override public String toString() {
		return "Katalog #" + getId();
	}

}

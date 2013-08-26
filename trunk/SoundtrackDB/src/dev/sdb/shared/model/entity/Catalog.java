package dev.sdb.shared.model.entity;

import dev.sdb.shared.model.db.Flavor;

public class Catalog extends AbstractEntity<Catalog> {

	private long parentId;
	private boolean catalogChildren;
	private boolean catalogEntries;
	private String title;
	private String info;
	private int eraBegin;
	private int eraEnd;

	public Catalog() {
		super();
	}

	public Catalog(long id, long parentId, boolean catalogChildren, boolean catalogEntries, String title, String info, int eraBegin, int eraEnd) {
		super(id);
		this.parentId = parentId;
		this.catalogChildren = catalogChildren;
		this.catalogEntries = catalogEntries;
		this.title = title;
		this.info = info;
		this.eraBegin = eraBegin;
		this.eraEnd = eraEnd;
	}

	@Override public Flavor getFlavor() {
		return Flavor.CATALOG;
	}

	@Override public String getMatch() {
		String match = getTitle();
		if ((this.info != null) && !this.info.isEmpty())
			match += " [" + this.info + "]";
		return match;
	}

	public boolean hasCatalogChildren() {
		return this.catalogChildren;
	}

	public boolean hasCatalogEntries() {
		return this.catalogEntries;
	}

	public boolean hasCatalogChildrenAndEntries() {
		return this.catalogChildren && this.catalogEntries;
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
		if (this.eraBegin > 0) {

			if (this.eraBegin == this.eraEnd)
				return String.valueOf(this.eraBegin);

			if (this.eraEnd > 0)
				return this.eraBegin + " - " + this.eraEnd;

			return "Ab " + this.eraBegin;

		}

		if (this.eraEnd > 0)
			return "Bis " + this.eraEnd;

		return null;
	}

	public long getParentId() {
		return this.parentId;
	}

}

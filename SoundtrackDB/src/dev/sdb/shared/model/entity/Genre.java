package dev.sdb.shared.model.entity;

import dev.sdb.shared.model.StatusChecker;
import dev.sdb.shared.model.db.Flavor;

public class Genre extends AbstractEntity<Genre> {

	private int status;
	private String parentName;
	private String childName;

	public Genre() {
		super();
	}

	public Genre(long id, int status, String parentName, String childName) {
		super(id);
		this.status = status;
		this.parentName = parentName;
		this.childName = childName;
	}

	@Override public Flavor getFlavor() {
		return Flavor.GENRE;
	}

	@Override public String getMatch() {
		return getName();
	}

	public String getName() {
		String name;
		if (this.childName != null && !this.childName.isEmpty())
			name = (this.parentName == null || this.parentName.isEmpty()) ? this.childName : (this.parentName + " " + this.childName);
		else
			name = this.parentName;
		return name;
	}

	/**
	 * @return the parentName
	 */
	public String getParentName() {
		return this.parentName;
	}

	/**
	 * @return the childName
	 */
	public String getChildName() {
		return this.childName;
	}

	public boolean isSoundtrackContainer() {
		return StatusChecker.isBitSet(this.status, 0);
	}
}

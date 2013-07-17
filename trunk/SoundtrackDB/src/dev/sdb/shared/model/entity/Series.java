package dev.sdb.shared.model.entity;

import dev.sdb.shared.model.StatusChecker;

public class Series extends AbstractEntity {

	private String title;
	private String shortTitle;
	private int editionStatus;
	private boolean singles;

	public Series() {
		super();
	}

	public Series(long id, String title, String shortTitle, int editionStatus, boolean singles) {
		super(id);
		this.title = title;
		this.shortTitle = shortTitle;
		this.editionStatus = editionStatus;
		this.singles = singles;
	}

	@Override public String getMatch() {
		return getTitle();
	}
	
	public String getTitleInfo() {
		if (this.singles)
			return null;
		return this.title;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return this.title;
	}

	/**
	 * @return the shortTitle
	 */
	public String getShortTitle() {
		return this.shortTitle;
	}

	/**
	 * @return the singles
	 */
	public boolean hasSingles() {
		return this.singles;
	}

	public boolean isBlacklisted() {
		return StatusChecker.isBitSet(this.editionStatus, 0);
	}

	@Override public String toString() {
		return "Series #" + getId();
	}
}

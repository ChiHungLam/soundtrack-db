package dev.sdb.shared.model.entity;

import dev.sdb.shared.model.StatusChecker;

public class Music extends AbstractEntity {

	private Genre genre;
	private String title;
	private String version;
	private int year;
	private String authors;
	private String artist;
	@SuppressWarnings("unused") private int partOrder;
	private int versionOrder;
	private int recStatus;
	private int partStatus;
	private int versionStatus;

	public Music() {
		super();
	}

	public Music(long id, Genre genre, String title, String version, int year, String authors, String artist, int partOrder, int versionOrder, int recStatus, int partStatus, int versionStatus) {
		super(id);
		this.genre = genre;
		this.title = title;
		this.version = version;
		this.year = year;
		this.authors = authors;
		this.artist = artist;
		this.partOrder = partOrder;
		this.versionOrder = versionOrder;
		this.recStatus = recStatus;
		this.partStatus = partStatus;
		this.versionStatus = versionStatus;
	}

	@Override public String getMatch() {
		return getTitleInfo();
	}

	@Override public String toString() {
		return "Music #" + getId();
	}

	public String getTitleInfo() {
		String info = this.title;
		if (!hasKnownName())
			info = "\"" + this.title + "\"";

		if ((this.versionOrder > 1 || !this.version.equals("Original Version")))
			info += " [" + this.version + "]";

		return info;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return this.title;
	}

	/**
	 * @return the genre
	 */
	public Genre getGenre() {
		return this.genre;
	}

	/**
	 * @return the version
	 */
	public String getVersion() {
		return this.version;
	}

	/**
	 * @return the year
	 */
	public int getYear() {
		return this.year;
	}

	public String getYearInfo() {
		if (this.year <= 0)
			return "";
		return (isApproximateYear() ? "ca." : "") + this.year;
	}

	/**
	 * @return the authors
	 */
	public String getAuthors() {
		return this.authors;
	}

	/**
	 * @return the artist
	 */
	public String getArtist() {
		return this.artist;
	}

	public boolean isOriginalSoundtrack() {
		return StatusChecker.isBitSet(this.versionStatus, 0);
	}

	public boolean isSoundtrackRemix() {
		return StatusChecker.isBitSet(this.versionStatus, 1);
	}

	public boolean isSoundtrackAlternate() {
		return StatusChecker.isBitSet(this.versionStatus, 2);
	}

	public boolean isApproximateYear() {
		return StatusChecker.isBitSet(this.versionStatus, 3);
	}

	public boolean hasAssumedPerformer() {
		return StatusChecker.isBitSet(this.versionStatus, 5);
	}

	public boolean isPlaceholderForUnidentified() {
		return StatusChecker.isBitSet(this.versionStatus, 6);
	}

	public boolean isComposedByMiller() {
		return StatusChecker.isBitSet(this.partStatus, 9);
	}

	public boolean isComposedByOthers() {
		return StatusChecker.isBitSet(this.partStatus, 10);
	}

	public boolean isTraditionalComposition() {
		return StatusChecker.isBitSet(this.partStatus, 11);
	}

	public boolean isPerformedByOthers() {
		return StatusChecker.isBitSet(this.partStatus, 12);
	}

	public boolean hasKnownName() {
		return StatusChecker.isBitSet(this.recStatus, 0);
	}

	public boolean isPartOfMedley() {
		return StatusChecker.isBitSet(this.recStatus, 8);
	}

	public boolean isPerformedByMiller() {
		return !isPerformedByOthers() || isComposedByMiller();
	}

}

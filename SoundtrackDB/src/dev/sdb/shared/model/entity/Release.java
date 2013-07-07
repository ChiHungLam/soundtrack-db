package dev.sdb.shared.model.entity;

import java.util.Date;

import dev.sdb.shared.model.StatusChecker;

public class Release extends AbstractEntity {

	private long audioId;
	private String artist;
	private String title;
	private String label;
	private String media;
	private String catalogNumber;
	private int print;
	private int year;
	private int printStatus;
	private Date duration;

	public Release() {
		super();
	}

	public Release(long id, long audioId, String artist, String title, String label, String media, String catalogNumber, int print, int year, int printStatus, Date duration) {
		super(id);
		this.audioId = audioId;
		this.artist = artist;
		this.title = title;
		this.label = label;
		this.media = media;
		this.catalogNumber = catalogNumber;
		this.print = print;
		this.year = year;
		this.printStatus = printStatus;
		this.duration = duration;
	}

	@Override public String toString() {
		String info = "Release #" + getId() + ": " + getTitle();
		return info;
	}

	/**
	 * @return the artist
	 */
	public String getArtist() {
		return this.artist;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return this.title;
	}

	/**
	 * @return the audioId
	 */
	public long getAudioId() {
		return this.audioId;
	}

	/**
	 * @return the media
	 */
	public String getMedia() {
		return this.media;
	}

	/**
	 * @return the catalogNumber
	 */
	public String getCatalogNumber() {
		return this.catalogNumber;
	}

	public String getCatalogInfo() {
		String info = "";
		if (this.label != null && !this.label.isEmpty())
			info += (info.isEmpty() ? "" : " ") + this.label;
		if (this.media != null && !this.media.isEmpty())
			info += (info.isEmpty() ? "" : " ") + this.media;
		if (this.catalogNumber != null && !this.catalogNumber.isEmpty())
			info += (info.isEmpty() ? "" : " ") + this.catalogNumber;
		if (this.print > 0)
			info += (info.isEmpty() ? "" : " ") + "(Aufl. " + this.print + ")";
		return info;
	}

	/**
	 * @return the label
	 */
	public String getLabel() {
		return this.label;
	}

	/**
	 * @return the print
	 */
	public int getPrint() {
		return this.print;
	}

	/**
	 * @return the year
	 */
	public int getYear() {
		return this.year;
	}

	public String getYearInfo() {
		if (this.year <= 0)
			return null;
		return (isYearApprox() ? "ca. " : "") + this.year;
	}
	/**
	 * @return the duration
	 */
	public Date getDuration() {
		return this.duration;
	}

	public boolean isYearApprox() {
		return StatusChecker.isBitSet(this.printStatus, 0);
	}

	public boolean isYearChecked() {
		return StatusChecker.isBitSet(this.printStatus, 1);
	}

	public boolean isUnreleased() {
		return StatusChecker.isBitSet(this.printStatus, 4);
	}

	public boolean isMusicCaptureComplete() {
		return StatusChecker.isBitSet(this.printStatus, 5);
	}

	public boolean hasMissingMusicValues() {
		return StatusChecker.isBitSet(this.printStatus, 6) // Missing music
				|| StatusChecker.isBitSet(this.printStatus, 7); // Missing time values
	}

	public boolean isMusicCaptureNotValidated() {
		return StatusChecker.isBitSet(this.printStatus, 8);
	}

	public boolean hasNullTimeValues() {
		return StatusChecker.isBitSet(this.printStatus, 9);
	}

	public boolean isMono() {
		return StatusChecker.isBitSet(this.printStatus, 10);
	}

	public boolean isStereoExplicitly() {
		return StatusChecker.isBitSet(this.printStatus, 11);
	}

	public boolean isVariousArtists() {
		return StatusChecker.isBitSet(this.printStatus, 13);
	}
}

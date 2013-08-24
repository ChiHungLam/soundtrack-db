package dev.sdb.shared.model.entity;

import dev.sdb.shared.model.StatusChecker;
import dev.sdb.shared.model.db.Flavor;

public class Release extends AbstractEntity<Release> {

	private String type;
	private Series series;
	private String artworkUrl;
	private int episode;
	//	private String artist;
	private String title;
	private String label;
	private String media;
	private String catalogNumber;
	private int print;
	private int year;
	private int typeStatus;
	private int productionStatus;
	private int releaseStatus;
	private int printStatus;
	private int durationSeconds;
	private long audioId;

	public Release() {
		super();
	}

	public Release(long id, String type, Series series, String artworkUrl, int episode, String title, String label, String media, String catalogNumber, int print, int year, int typeStatus, int productionStatus, int releaseStatus, int printStatus, int durationSeconds, long audioId) {
		super(id);
		this.type = type;
		this.series = series;
		this.artworkUrl = artworkUrl;
		this.episode = episode;
		//		this.artist = artist;
		this.title = title;
		this.label = label;
		this.media = media;
		this.catalogNumber = catalogNumber;
		this.print = print;
		this.year = year;
		this.typeStatus = typeStatus;
		this.productionStatus = productionStatus;
		this.releaseStatus = releaseStatus;
		this.printStatus = printStatus;
		this.durationSeconds = durationSeconds;
		this.audioId = audioId;
	}

	@Override public Flavor getFlavor() {
		return Flavor.RELEASE;
	}

	@Override public String getMatch() {
		return getTitleInfo();
	}

	public String getArtworkUrl() {
		return this.artworkUrl;
	}
	
	public String getTitleInfo() {
		String info;

		String preInfo = getSeries().getTitleInfo();
		//		String postInfo = canContainSoundtrack() ? null : getArtist();

		if (preInfo == null || preInfo.isEmpty()) {
			info = this.title;
		} else if (preInfo.equals(this.title)) {
			info = this.title;
		} else {

			int len = preInfo.length();

			String title;
			if (this.title.startsWith(preInfo) && (this.title.length() > (len + 1)) && (this.title.charAt(len) == ' ')) {
				title = this.title.substring(len + 1);
			} else {
				title = this.title;
			}

			String episode;
			if (this.episode > 0)
				episode = " (" + this.episode + ")";
			else
				episode = preInfo.contains(title) ? "" : ":";

			info = preInfo + episode;

			if (!info.contains(title))
				info += " " + title;
		}

		//		if (postInfo != null && !postInfo.isEmpty() && !info.contains(postInfo)) {
		//			info += " (" + postInfo + ")";
		//		}

		return info;
	}



	//	public String getArtistInfo() {
	//		if (isVariousArtists())
	//			return "V.A.";
	//		else
	//			return getArtist();
	//	}

	public String getYearInfo() {
		if (this.year <= 0)
			return null;
		return (isYearApprox() ? "ca. " : "") + this.year;
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
	 * @return the type
	 */
	public String getType() {
		return this.type;
	}

	/**
	 * @return the series
	 */
	public Series getSeries() {
		return this.series;
	}

	/**
	 * @return the episode
	 */
	public int getEpisode() {
		return this.episode;
	}

	//	/**
	//	 * @return the artist
	//	 */
	//	public String getArtist() {
	//		return this.artist;
	//	}

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

	/**
	 * @return the durationSeconds
	 */
	public int getDurationSeconds() {
		return this.durationSeconds;
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

	//	public boolean isVariousArtists() {
	//		return StatusChecker.isBitSet(this.printStatus, 13);
	//	}

	public boolean hasAssumedCatalogNumber() {
		return StatusChecker.isBitSet(this.releaseStatus, 10);
	}

	public boolean isDigiPack() {
		return StatusChecker.isBitSet(this.releaseStatus, 11);
	}

	public boolean isBoxSet() {
		return StatusChecker.isBitSet(this.releaseStatus, 12);
	}

	public boolean isSlipcase() {
		return StatusChecker.isBitSet(this.releaseStatus, 13);
	}

	public boolean isBlacklisted() {
		return StatusChecker.isBitSet(this.productionStatus, 1) || getSeries().isBlacklisted();
	}

	public boolean isContainingRemixSoundtrack() {
		return StatusChecker.isBitSet(this.productionStatus, 6);
	}

	public boolean isContainingAlternateSoundtrack() {
		return StatusChecker.isBitSet(this.productionStatus, 7);
	}

	public boolean isContainingOriginalSoundtrack() {
		return StatusChecker.isBitSet(this.productionStatus, 11);
	}

	public boolean canContainSoundtrack() {
		return StatusChecker.isBitSet(this.typeStatus, 0);
	}
}

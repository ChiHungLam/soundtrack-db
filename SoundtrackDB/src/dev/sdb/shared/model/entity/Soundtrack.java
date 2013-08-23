package dev.sdb.shared.model.entity;


public class Soundtrack extends AbstractEntity<Soundtrack> {

	private Release release;
	private Music music;
	private int mediaIndex;
	private String seqNum;

	private int startTime;
	private int stopTime;

	public Soundtrack() {
		super();
	}

	public Soundtrack(long id, Release release, Music music, int mediaIndex, String seqNum, int startTime, int stopTime) {
		super(id);
		this.release = release;
		this.music = music;
		this.mediaIndex = mediaIndex;
		this.seqNum = seqNum;
		this.startTime = startTime;
		this.stopTime = stopTime;
	}

	@Override public String getMatch() {
		return "#" + getId();
	}

	public int getStartTime() {
		return this.startTime;
	}

	public int getStopTime() {
		return this.stopTime;
	}

	public int getMediaIndex() {
		return this.mediaIndex;
	}

	public String getSeqNum() {
		return this.seqNum;
	}

	public Release getRelease() {
		return this.release;
	}

	public void setRelease(Release release) {
		this.release = release;
	}

	public Music getMusic() {
		return this.music;
	}

	public void setMusic(Music music) {
		this.music = music;
	}

	@Override public String toString() {
		return "Soundtrack #" + getId();
	}
}

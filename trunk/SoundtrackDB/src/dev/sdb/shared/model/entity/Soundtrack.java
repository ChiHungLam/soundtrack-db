package dev.sdb.shared.model.entity;

public class Soundtrack extends AbstractEntity {

	private Release release;
	private Music music;

	public Soundtrack() {
		super();
	}

	@Override public String getMatch() {
		return "#" + getId();
	}

	public Soundtrack(long id, Release release, Music music) {
		super(id);
		this.release = release;
		this.music = music;
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

package dev.sdb.shared.model.entity;

public class Soundtrack extends AbstractEntity {

	private Release release;
	private Music music;

	public Soundtrack() {
		super();
	}

	public Soundtrack(long id, Release release, Music music) {
		super(id);
		this.release = release;
		this.music = music;
	}

	public Release getRelease() {
		return this.release;
	}

	public Music getMusic() {
		return this.music;
	}

	@Override public String toString() {
		String info = "Soundtrack #" + getId();
		info += " - " + ((this.release == null) ? "no release" : this.release);
		info += " - " + ((this.music == null) ? "no music" : this.music);
		return info;
	}
}

package dev.sdb.shared.model.entity;

public class Soundtrack extends AbstractEntity {

	private Release release;
	private Music music;

	public Soundtrack() {
		super();
	}

	public Soundtrack(long id, Release release, Music music) {
		super(id);
	}

	@Override public String toString() {
		return "Soundtrack - id " + getId();
	}

	public Release getRelease() {
		return this.release;
	}

	public Music getMusic() {
		return this.music;
	}
}

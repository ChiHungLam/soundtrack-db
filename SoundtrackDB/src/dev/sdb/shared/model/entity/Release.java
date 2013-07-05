package dev.sdb.shared.model.entity;

public class Release extends AbstractEntity {

	private String title;
	private long audioId;

	public Release() {
		super();
	}

	public Release(long id, String title, long audioId) {
		super(id);
		this.title = title;
		this.audioId = audioId;
	}

	public String getTitle() {
		return this.title;
	}

	public long getAudioId() {
		return this.audioId;
	}

	@Override public String toString() {
		String info = "Release #" + getId() + ": " + getTitle();
		return info;
	}
}

package dev.sdb.shared.model;

public abstract class AbstractSoundtrackContainer implements SoundtrackContainer {

	private String title;

	public AbstractSoundtrackContainer() {
		super();
	}

	public AbstractSoundtrackContainer(String title) {
		super();
		this.title = title;
	}

	@Override public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
}

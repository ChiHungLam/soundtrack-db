package dev.sdb.shared.model;

public abstract class AbstractSoundtrackContainer extends AbstractEntity implements SoundtrackContainer {


	private String title;

	public AbstractSoundtrackContainer() {
		super();
	}

	public AbstractSoundtrackContainer(long id, String title) {
		super(id);
		this.title = title;
	}

	@Override public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
}

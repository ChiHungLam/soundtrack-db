package dev.sdb.shared.model;

public class Release extends AbstractSoundtrackContainer {

	public Release() {
		super();
	}

	public Release(long id, String title) {
		super(id, title);
	}

	@Override public SoundtrackContainerType getType() {
		return SoundtrackContainerType.RELEASE;
	}
}

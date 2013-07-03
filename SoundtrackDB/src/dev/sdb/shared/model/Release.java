package dev.sdb.shared.model;

public class Release extends AbstractSoundtrackContainer {

	public Release() {
		super();
	}

	public Release(String title) {
		super(title);
	}

	@Override public SoundtrackContainerType getType() {
		return SoundtrackContainerType.RELEASE;
	}
}

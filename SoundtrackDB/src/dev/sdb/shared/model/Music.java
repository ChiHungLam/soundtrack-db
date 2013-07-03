package dev.sdb.shared.model;


public class Music extends AbstractSoundtrackContainer {

	public Music() {
		super();
	}

	public Music(String title) {
		super(title);
	}

	@Override public SoundtrackContainerType getType() {
		return SoundtrackContainerType.MUSIC;
	}
}

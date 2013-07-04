package dev.sdb.shared.model;


public class Music extends AbstractSoundtrackContainer {

	public Music() {
		super();
	}

	public Music(long id, String title) {
		super(id, title);
	}

	@Override public SoundtrackContainerType getType() {
		return SoundtrackContainerType.MUSIC;
	}
}

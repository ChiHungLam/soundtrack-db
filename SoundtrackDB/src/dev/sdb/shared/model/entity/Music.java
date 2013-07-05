package dev.sdb.shared.model.entity;

public class Music extends AbstractEntity {

	private String title;

	public Music() {
		super();
	}

	public Music(long id, String title) {
		super(id);
		this.title = title;
	}

	public String getTitle() {
		return this.title;
	}

	//	@Override public SoundtrackContainerType getType() {
	//		return SoundtrackContainerType.MUSIC;
	//	}
}

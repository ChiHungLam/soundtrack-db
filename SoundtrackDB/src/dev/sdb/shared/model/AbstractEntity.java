package dev.sdb.shared.model;

public abstract class AbstractEntity implements Entity {

	private long id;

	public AbstractEntity() {
		super();
	}

	public AbstractEntity(long id) {
		super();
		this.id = id;
	}

	@Override public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

}

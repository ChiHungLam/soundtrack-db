package dev.sdb.shared.model.entity;

public abstract class AbstractEntity implements Entity {

	private long id;

	public AbstractEntity() {
		super();
	}

	public AbstractEntity(long id) {
		super();
		this.id = id;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dev.sdb.shared.model.entity.Entity#getId()
	 */
	@Override public long getId() {
		return this.id;
	}


}

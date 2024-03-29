package dev.sdb.shared.model.entity;


public abstract class AbstractEntity<T> implements Entity {

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

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override public String toString() {
		return getFlavor().name() + " #" + getId();
	}

}

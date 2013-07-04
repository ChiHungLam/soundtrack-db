package dev.sdb.shared.model;

public abstract class AbstractModel {

	private long id;

	public AbstractModel() {
		super();
	}

	public AbstractModel(long id) {
		super();
		this.id = id;
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

}

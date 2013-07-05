package dev.sdb.shared.model;

public class Soundtrack extends AbstractEntity {

	public Soundtrack() {
		super();
	}

	public Soundtrack(long id) {
		super(id);
	}

	@Override public String toString() {
		return "Soundtrack - id " + getId();
	}
}

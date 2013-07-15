package dev.sdb.shared.model.db;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

import dev.sdb.shared.model.entity.Entity;

public class Result implements IsSerializable {

	private List<Entity> resultChunk;
	private int rangeStart;
	private int totalLength;

	public Result() {
		super();
	}

	public Result(List<Entity> resultChunk, int rangeStart, int totalLength) {
		super();
		this.resultChunk = resultChunk;
		this.rangeStart = rangeStart;
		this.totalLength = totalLength;
	}

	public List<Entity> getResultChunk() {
		return this.resultChunk;
	}

	public int getTotalLength() {
		return this.totalLength;
	}

	public int getRangeStart() {
		return this.rangeStart;
	}
}

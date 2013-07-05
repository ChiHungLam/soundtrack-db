package dev.sdb.shared.model.db;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

import dev.sdb.shared.model.entity.Entity;

public class Result implements IsSerializable {

	private List<Entity> resultChunk;
	private int totalLength;

	public Result() {
		super();
	}

	public Result(List<Entity> resultChunk, int totalLength) {
		super();
		this.resultChunk = resultChunk;
		this.totalLength = totalLength;
	}

	public List<Entity> getResultChunk() {
		return this.resultChunk;
	}

	public int getTotalLength() {
		return this.totalLength;
	}


}

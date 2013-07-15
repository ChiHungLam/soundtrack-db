package dev.sdb.shared.model.db;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

import dev.sdb.shared.model.entity.Entity;

public class Result implements IsSerializable {

	private List<Entity> resultChunk;
	private int totalLength;
	private String term;
	private int resultStart;

	public Result() {
		super();
	}

	public Result(String term, List<Entity> resultChunk, int resultStart, int totalLength) {
		super();
		this.term = term;
		this.resultChunk = resultChunk;
		this.resultStart = resultStart;
		this.totalLength = totalLength;
	}

	public List<Entity> getResultChunk() {
		return this.resultChunk;
	}

	public int getTotalLength() {
		return this.totalLength;
	}

	public String getTerm() {
		return this.term;
	}

	public int getResultStart() {
		return this.resultStart;
	}

}

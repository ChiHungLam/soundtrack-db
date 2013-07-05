package dev.sdb.shared.model.db;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

import dev.sdb.shared.model.entity.Entity;

public class SearchResult implements IsSerializable {

	private String info;
	private List<Entity> resultChunk;
	private int totalLength;

	public SearchResult() {
		super();
	}

	public SearchResult(String info, List<Entity> resultChunk, int totalLength) {
		super();
		this.info = info;
		this.resultChunk = resultChunk;
		this.totalLength = totalLength;
	}

	public String getInfo() {
		return this.info;
	}

	public List<Entity> getResultChunk() {
		return this.resultChunk;
	}

	public int getTotalLength() {
		return this.totalLength;
	}


}

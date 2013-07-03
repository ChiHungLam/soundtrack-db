package dev.sdb.shared.model;

import com.google.gwt.user.client.rpc.IsSerializable;

public class SearchResultSort implements IsSerializable {

	private SearchResultSortType type;
	private boolean ascending;

	public SearchResultSort() {
		super();
	}

	public SearchResultSort(SearchResultSortType type, boolean ascending) {
		super();
		this.type = type;
		this.ascending = ascending;
	}

	public boolean isAscending() {
		return this.ascending;
	}

	public SearchResultSortType getType() {
		return this.type;
	}

}

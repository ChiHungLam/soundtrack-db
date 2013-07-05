package dev.sdb.client.ui.search;

import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasText;

import dev.sdb.shared.model.Entity;
import dev.sdb.shared.model.SearchResultSortType;

public abstract class AbstractResultField extends Composite implements HasText {

	public AbstractResultField() {
		super();
	}

	public abstract CellTable<Entity> getTable();

	public abstract SearchResultSortType getSortType();

	public abstract void setElementVisibility(int total);

}

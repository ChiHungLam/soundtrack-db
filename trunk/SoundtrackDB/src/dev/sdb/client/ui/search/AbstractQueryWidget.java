package dev.sdb.client.ui.search;

import com.google.gwt.user.client.ui.Composite;

public abstract class AbstractQueryWidget extends Composite {

	public abstract SearchField getSearchField();

	public abstract AbstractResultField getResultField();

}

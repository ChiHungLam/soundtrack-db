package dev.sdb.client.view.desktop.detail;

import com.google.gwt.user.cellview.client.ColumnSortEvent.AsyncHandler;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.Range;
import com.google.gwt.view.client.SingleSelectionModel;

import dev.sdb.client.view.DetailView;
import dev.sdb.client.view.desktop.detail.master.MasterDataWidget;
import dev.sdb.client.view.desktop.detail.sublist.SublistWidget;
import dev.sdb.shared.model.db.Result;
import dev.sdb.shared.model.entity.Entity;

public abstract class DetailWidget extends Composite implements DetailView {

	private DetailView.Presenter presenter;

	public DetailWidget() {
		super();
	}

	protected abstract MasterDataWidget getMasterDataWidget();

	public abstract void initEntity(Entity entity);

	protected DetailView.Presenter getPresenter() {
		return this.presenter;
	}
	@Override public void setPresenter(DetailView.Presenter presenter) {
		this.presenter = presenter;
	}

	protected void initSublist() {

		DataGrid<Entity> table = getSublistTable();

		// Set the total row count. You might send an RPC request to determine the
		// total row count.
		table.setRowCount(0, true);

		// Set the range to display. In this case, our visible range is smaller than
		// the data set.
		int rangeLength = 10;
		table.setVisibleRange(0, rangeLength);

		// Add a ColumnSortEvent.AsyncHandler to connect sorting to the
		// AsyncDataPRrovider.
		AsyncHandler columnSortHandler = new AsyncHandler(table);
		table.addColumnSortHandler(columnSortHandler);

		final SingleSelectionModel<Entity> selectionModel = new SingleSelectionModel<Entity>();
		table.setSelectionModel(selectionModel);
	}

	@Override public void setSublistDataProvider(AsyncDataProvider<Entity> dataProvider) {
		dataProvider.addDataDisplay(getSublist().getTable());
	}

	@Override public void clearSublist() {
		getSublist().setElementVisibility(-1);
	}

	@Override public Range getSublistRange() {
		return getSublist().getTable().getVisibleRange();
	}

	@Override public void showSublistResult(String resultInfo, Result searchResult) {
		getSublist().getTable().setRowCount(searchResult.getTotalLength(), true);
		getSublist().getTable().setRowData(searchResult.getRangeStart(), searchResult.getResultChunk());

		getSublist().setSelectionInfoText(resultInfo);
		getSublist().setElementVisibility(searchResult.getTotalLength());
	}

	protected abstract SublistWidget getSublist();

	@Override public boolean isEnabled() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override public void setEnabled(boolean enabled) {
		// TODO Auto-generated method stub
	}
}

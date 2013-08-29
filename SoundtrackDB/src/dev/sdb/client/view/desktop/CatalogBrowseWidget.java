package dev.sdb.client.view.desktop;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasVisibility;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;

import dev.sdb.client.view.CatalogBrowseView;
import dev.sdb.shared.model.db.Result;
import dev.sdb.shared.model.entity.Catalog;
import dev.sdb.shared.model.entity.Entity;


public class CatalogBrowseWidget extends Composite implements CatalogBrowseView {

	//	private static final int VISIBLE_RANGE_LENGTH = 10;

	interface CatalogBrowseWidgetUiBinder extends UiBinder<Widget, CatalogBrowseWidget> {}
	private static CatalogBrowseWidgetUiBinder uiBinder = GWT.create(CatalogBrowseWidgetUiBinder.class);

	@UiField(provided = true) CatalogTreeWidget catalogTree;

	@UiField(provided = true) CellTable<Entity> releaseTable;
	@UiField(provided = true) SimplePager releasePager;
	@UiField HasVisibility releaseTableScroll;

	@UiField Label selectionInfoLabel;

	private Presenter presenter;

	public CatalogBrowseWidget(Presenter presenter) {
		super();
		setPresenter(presenter);

		createCatalogTree();

		createReleaseTable();

		initWidget(uiBinder.createAndBindUi(this));


		// Set the total row count. You might send an RPC request to determine the
		//		 total row count.
		this.releaseTable.setRowCount(0, true);

		// Set the range to display. In this case, our visible range is smaller than
		// the data set.
		this.releaseTable.setVisibleRange(0, Integer.MAX_VALUE);

		// Create a data provider.
		AsyncDataProvider<Entity> dataProvider = new AsyncDataProvider<Entity>() {
			@Override protected void onRangeChanged(HasData<Entity> display) {
				Catalog catalog = CatalogBrowseWidget.this.catalogTree.getSelectedCatalog();
				if (catalog == null)
					return;
				CatalogBrowseWidget.this.presenter.getCatalogReleases(
						catalog,
						CatalogBrowseWidget.this.releaseTable.getVisibleRange(),
						CatalogBrowseWidget.this);
			}
		};

		dataProvider.addDataDisplay(this.releaseTable);

		setReleaseTableVisibility(false);
	}

	private void setReleaseTableVisibility(boolean visible) {
		this.releaseTableScroll.setVisible(visible);
		this.releasePager.setVisible(visible);
	}

	@Override public CellTable<Entity> getReleaseTable() {
		return this.releaseTable;
	}

	private void createCatalogTree() {
		this.catalogTree = new CatalogTreeWidget(this.presenter);
		this.catalogTree.setListener(new CatalogTreeWidget.Listener() {
			@Override public void onSelection(Entity catalog) {
				onCatalogSelection((Catalog) catalog);
			}
		});
	}
	private void createReleaseTable() {
		this.releaseTable = new CellTable<Entity>();

		SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
		this.releasePager = new SimplePager(TextLocation.CENTER, pagerResources, true, 1000, true);

		this.releasePager.setDisplay(this.releaseTable);
	}

	@Override public void showResult(Result catalogResult) {
		int total = catalogResult.getTotalLength();

		String resultInfo = "";
		if (total == 0) {
			resultInfo = "Es wurden keine Einträge gefunden.";
		} else if (total > 0) {
			resultInfo = "Es " + (total == 1 ? "wurde 1 Eintrag" : ("wurden " + total + " Einträge")) + " gefunden.";
		}

		this.releaseTable.setRowCount(total, true);
		this.releaseTable.setRowData(catalogResult.getRangeStart(), catalogResult.getResultChunk());
		//this.releaseTable.setVisibleRange(0, total);

		if (total > 0)
			this.releaseTable.getRowElement(0).scrollIntoView(); // scroll to top

		this.selectionInfoLabel.setText(resultInfo);

		setReleaseTableVisibility(total > 0);
	}

	protected void onCatalogSelection(Catalog catalog) {

		setReleaseTableVisibility(catalog != null);

		if (catalog == null) {
			this.releaseTable.setRowCount(0, true);
			//this.releaseTable.setVisibleRange(0, VISIBLE_RANGE_LENGTH);
			//			this.releaseTable.setRowData(0, );
			return;
		}

		this.presenter.getCatalogReleases(
				catalog,
				this.releaseTable.getVisibleRange(),
				this);
	}

	private void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}



}

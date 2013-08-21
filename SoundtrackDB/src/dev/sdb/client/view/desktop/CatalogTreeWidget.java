package dev.sdb.client.view.desktop;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.OnlyToBeUsedInGeneratedCodeStringBlessedAsSafeHtml;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellTree;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasVisibility;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.gwt.view.client.TreeViewModel;

import dev.sdb.client.view.CatalogTreeView;
import dev.sdb.shared.model.db.Result;
import dev.sdb.shared.model.entity.Catalog;
import dev.sdb.shared.model.entity.Entity;

/**
 * Implementation of CellTree widget is based on:
 * http://google-web-toolkit.googlecode.com/svn-history/r9108/trunk/samples/expenses
 * /src/main/java/com/google/gwt/sample/expenses/client/ExpenseTree.java
 * 
 * @author s.christ
 * 
 */
public class CatalogTreeWidget extends Composite implements CatalogTreeView {

	private static final int VISIBLE_RANGE_LENGTH = 10;

	interface CatalogTreeWidgetUiBinder extends UiBinder<Widget, CatalogTreeWidget> {}
	private static CatalogTreeWidgetUiBinder uiBinder = GWT.create(CatalogTreeWidgetUiBinder.class);

	/**
	 * Custom listener for this widget.
	 */
	public interface Listener {

		/**
		 * Called when the user selects a tree item.
		 * 
		 * @param catalog
		 *            the selected catalog
		 */
		void onSelection(Entity catalog);
	}

	/**
	 * The listener of this widget.
	 */
	private Listener listener;

	/**
	 * The shared {@link SingleSelectionModel}.
	 */
	private final SingleSelectionModel<Entity> selectionModel = new SingleSelectionModel<Entity>(new ProvidesKey<Entity>() {

		private final ProvidesKey<Entity> keyProvider = new ProvidesKey<Entity>() {
			@Override public Long getKey(Entity entity) {
				return (entity == null) ? null : Long.valueOf(entity.getId());
			}
		};

		public Long getKey(Entity entity) {
			return (Long) this.keyProvider.getKey(entity);
		}
	});

	public void setListener(Listener listener) {
		this.listener = listener;
	}

	/**
	 * The {@link TreeViewModel} used to browse catalogs.
	 */
	private class CatalogTreeViewModel implements TreeViewModel {

		/**
		 * The catalogCell singleton.
		 */
		private final Cell<Entity> catalogCell = new AbstractCell<Entity>() {

			@Override public void render(Cell.Context context, Entity value, SafeHtmlBuilder sb) {
				Catalog catalog = (Catalog) value;
				String info = catalog.getInfo();
				String era = catalog.getEra();

				String html = "<table><tr>"
						+ "<td style='text-align: left; width: 100%;'><span style='font-weight: bold;'>" + catalog.getTitle() + "</span>";
				if ((info != null) && !info.isEmpty())
					html += "&nbsp;<span style='font-size: smaller;'>[" + info + "]</span>";
				html += "</td><td style='text-align: right; width: 100px;'>";
				if ((era != null) && !era.isEmpty())
					html += "<span style='font-size: smaller;'>(" + era.replace(" ", "&nbsp;") + ")</span>";
				html += "</td></tr></table>";

				SafeHtml sh = new OnlyToBeUsedInGeneratedCodeStringBlessedAsSafeHtml(html);
				sb.append(sh);
			}

		};

		public <T> NodeInfo<?> getNodeInfo(T value) {
			if (value == null) {
				// Top level.
				CatalogListDataProvider dataProvider = new CatalogListDataProvider(0);
				return new DefaultNodeInfo<Entity>(dataProvider, this.catalogCell, CatalogTreeWidget.this.selectionModel, null);
			} else if (value instanceof Entity) {
				// Any other level.
				CatalogListDataProvider dataProvider = new CatalogListDataProvider(((Entity) value).getId());
				return new DefaultNodeInfo<Entity>(dataProvider, this.catalogCell, CatalogTreeWidget.this.selectionModel, null);
			}

			return null;
		}

		public boolean isLeaf(Object value) {
			return value != null && (value instanceof Catalog) && !((Catalog) value).hasChildren();
		}
	}

	/**
	 * The {@link ListDataProvider} used for Catalog lists.
	 */
	private class CatalogListDataProvider extends AsyncDataProvider<Entity> {

		private final long parentId;

		public CatalogListDataProvider(long parentId) {
			super(null);
			this.parentId = parentId;
		}

		@Override protected void onRangeChanged(HasData<Entity> view) {
			CatalogTreeWidget.this.presenter.getCatalogLevelEntries(this.parentId, this);
		}
	}

	@UiField(provided = true) CellTree cellTree;

	@UiField(provided = true) DataGrid<Entity> dataGrid;
	@UiField(provided = true) SimplePager pager;

	@UiField Label selectionInfoLabel;
	@UiField HasVisibility tablePanel;

	/**
	 * The last selected catalog.
	 */
	private Catalog lastCatalog;

	private Presenter presenter;

	public CatalogTreeWidget(Presenter presenter) {
		super();
		setPresenter(presenter);

		createCatalogTree();

		createReleaseGrid();

		initWidget(uiBinder.createAndBindUi(this));


		// Set the total row count. You might send an RPC request to determine the
		//		 total row count.
		this.dataGrid.setRowCount(0, true);

		// Set the range to display. In this case, our visible range is smaller than
		// the data set.
		this.dataGrid.setVisibleRange(0, VISIBLE_RANGE_LENGTH);

		UiFactoryImpl.addReleaseColumns(this.dataGrid, true, false);

		// Create a data provider.
		AsyncDataProvider<Entity> dataProvider = new AsyncDataProvider<Entity>() {
			@Override protected void onRangeChanged(HasData<Entity> display) {
				if (CatalogTreeWidget.this.lastCatalog == null)
					return;
				CatalogTreeWidget.this.presenter.getCatalogReleases(
						CatalogTreeWidget.this.lastCatalog,
						CatalogTreeWidget.this.dataGrid.getVisibleRange(),
						CatalogTreeWidget.this);
			}
		};

		dataProvider.addDataDisplay(this.dataGrid);

		this.tablePanel.setVisible(false);
	}

	private void createReleaseGrid() {
		this.dataGrid = new DataGrid<Entity>();

		SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
		this.pager = new SimplePager(TextLocation.CENTER, pagerResources, true, 1000, true);

		this.pager.setDisplay(this.dataGrid);


	}

	@Override public void showResult(Result catalogResult) {
		int total = catalogResult.getTotalLength();

		String resultInfo = "";
		if (total == 0) {
			resultInfo = "Es wurden keine Einträge gefunden.";
		} else if (total > 0) {
			resultInfo = "Es " + (total == 1 ? "wurde 1 Eintrag" : ("wurden " + total + " Einträge")) + " gefunden.";
		}

		this.dataGrid.setRowCount(total, true);
		this.dataGrid.setRowData(catalogResult.getRangeStart(), catalogResult.getResultChunk());
		this.selectionInfoLabel.setText(resultInfo);
		this.dataGrid.setVisible((total > 0));
		this.pager.setVisible((total > 0));

		this.tablePanel.setVisible(true);
	}

	protected void onCatalogSelection() {
		this.tablePanel.setVisible(this.lastCatalog != null);
		if (this.lastCatalog == null) {
			this.tablePanel.setVisible(false);
			this.dataGrid.setRowCount(0, true);
			this.dataGrid.setVisibleRange(0, VISIBLE_RANGE_LENGTH);
			//			this.dataGrid.setRowData(0, );
			return;
		}

		this.presenter.getCatalogReleases(
				this.lastCatalog,
				this.dataGrid.getVisibleRange(),
				this);
	}

	private void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	/**
	 * Create the {@link CellTree}.
	 */
	private void createCatalogTree() {
		final CatalogTreeViewModel model = new CatalogTreeViewModel();

		// Listen for selection. We need to add this handler before the CellBrowser
		// adds its own handler.
		this.selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			public void onSelectionChange(SelectionChangeEvent event) {
				Object selected = CatalogTreeWidget.this.selectionModel.getSelectedObject();
				if (selected == null) {
					CatalogTreeWidget.this.lastCatalog = null;
				} else if (selected instanceof Entity) {
					CatalogTreeWidget.this.lastCatalog = (Catalog) selected;
				}

				onCatalogSelection();

				if (CatalogTreeWidget.this.listener != null) {
					CatalogTreeWidget.this.listener.onSelection(CatalogTreeWidget.this.lastCatalog);
				}
			}
		});

		CellTree.Resources resources = GWT.create(CellTree.BasicResources.class);

		// Create a CellBrowser.
		this.cellTree = new CellTree(model, null, resources);
		this.cellTree.setAnimationEnabled(true);

	}

}
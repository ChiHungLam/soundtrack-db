package dev.sdb.client.view.desktop;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellTree;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.gwt.view.client.TreeViewModel;

import dev.sdb.client.view.CatalogBrowseView.Presenter;
import dev.sdb.client.view.UiFactory.HtmlFactory;
import dev.sdb.shared.model.entity.Catalog;
import dev.sdb.shared.model.entity.Entity;

/**
 * Implementation of CellTree widget is based on:
 * http://google-web-toolkit.googlecode.com/svn-history/r9108/trunk/samples/expenses
 * /src/main/java/com/google/gwt/sample/expenses/client/ExpenseTree.java
 */
public class CatalogTreeWidget extends Composite {

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

				HtmlFactory htmlFactory = CatalogTreeWidget.this.presenter.getHtmlFactory();

				SafeHtml html = htmlFactory.getCatalogTreeEntry(catalog);
				sb.append(html);
			}

		};

		public <T> NodeInfo<?> getNodeInfo(T value) {
			if (value == null) {
				// Top level.
				CatalogListDataProvider dataProvider = new CatalogListDataProvider(0);
				return new DefaultNodeInfo<Entity>(dataProvider, this.catalogCell, CatalogTreeWidget.this.treeSelectionModel, null);
			} else if (value instanceof Entity) {
				// Any other level.
				CatalogListDataProvider dataProvider = new CatalogListDataProvider(((Entity) value).getId());
				return new DefaultNodeInfo<Entity>(dataProvider, this.catalogCell, CatalogTreeWidget.this.treeSelectionModel, null);
			}

			return null;
		}

		public boolean isLeaf(Object value) {
			return value != null && (value instanceof Catalog) && !((Catalog) value).hasCatalogChildren();
		}
	}

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

	interface CatalogTreeWidgetUiBinder extends UiBinder<Widget, CatalogTreeWidget> {}
	private static CatalogTreeWidgetUiBinder uiBinder = GWT.create(CatalogTreeWidgetUiBinder.class);

	/**
	 * The shared {@link SingleSelectionModel}.
	 */
	private final SingleSelectionModel<Entity> treeSelectionModel = new SingleSelectionModel<Entity>(new ProvidesKey<Entity>() {

		private final ProvidesKey<Entity> keyProvider = new ProvidesKey<Entity>() {
			@Override public Long getKey(Entity entity) {
				return (entity == null) ? null : Long.valueOf(entity.getId());
			}
		};

		public Long getKey(Entity entity) {
			return (Long) this.keyProvider.getKey(entity);
		}
	});



	/**
	 * The last selected catalog.
	 */
	private Catalog selectedCatalog;

	private Presenter presenter;

	/**
	 * The listener of this widget.
	 */
	private Listener listener;

	@UiField(provided = true) CellTree tree;

	public CatalogTreeWidget(Presenter presenter) {
		super();
		setPresenter(presenter);

		createCatalogTree();

		initWidget(uiBinder.createAndBindUi(this));
	}

	/**
	 * Create the {@link CellTree}.
	 */
	private void createCatalogTree() {
		final CatalogTreeViewModel treeViewModel = new CatalogTreeViewModel();

		// Listen for selection. We need to add this handler before the CellBrowser
		// adds its own handler.
		this.treeSelectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			public void onSelectionChange(SelectionChangeEvent event) {
				Object selected = CatalogTreeWidget.this.treeSelectionModel.getSelectedObject();
				if (selected == null) {
					CatalogTreeWidget.this.selectedCatalog = null;
				} else if (selected instanceof Entity) {
					CatalogTreeWidget.this.selectedCatalog = (Catalog) selected;
				}

				if (CatalogTreeWidget.this.listener != null) {
					CatalogTreeWidget.this.listener.onSelection(CatalogTreeWidget.this.selectedCatalog);
				}
			}
		});

		CellTree.Resources resources = GWT.create(CellTree.BasicResources.class);

		// Create a CellTree.
		this.tree = new CellTree(treeViewModel, null, resources);
		this.tree.setAnimationEnabled(true);

	}

	private void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	public void setListener(Listener listener) {
		this.listener = listener;
	}

	public Catalog getSelectedCatalog() {
		return this.selectedCatalog;
	}
}

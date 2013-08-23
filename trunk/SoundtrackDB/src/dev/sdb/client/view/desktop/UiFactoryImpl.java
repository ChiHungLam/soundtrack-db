package dev.sdb.client.view.desktop;

import com.google.gwt.cell.client.ActionCell;
import com.google.gwt.cell.client.SafeHtmlCell;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.http.client.URL;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.cellview.client.TextColumn;

import dev.sdb.client.ClientFactory;
import dev.sdb.client.presenter.ContentPresenterType;
import dev.sdb.client.view.CatalogDetailView;
import dev.sdb.client.view.CatalogTreeView;
import dev.sdb.client.view.ErrorView;
import dev.sdb.client.view.FooterView;
import dev.sdb.client.view.HeaderView;
import dev.sdb.client.view.HomeView;
import dev.sdb.client.view.MusicDetailView;
import dev.sdb.client.view.MusicQueryView;
import dev.sdb.client.view.NavigatorView;
import dev.sdb.client.view.ReleaseDetailView;
import dev.sdb.client.view.ReleaseQueryView;
import dev.sdb.client.view.SectionInfoView;
import dev.sdb.client.view.SeriesDetailView;
import dev.sdb.client.view.SeriesQueryView;
import dev.sdb.client.view.SoundtrackDetailView;
import dev.sdb.client.view.SoundtrackQueryView;
import dev.sdb.client.view.UiFactory;
import dev.sdb.client.view.desktop.detail.CatalogDetailWidget;
import dev.sdb.client.view.desktop.detail.MusicDetailWidget;
import dev.sdb.client.view.desktop.detail.ReleaseDetailWidget;
import dev.sdb.client.view.desktop.detail.SeriesDetailWidget;
import dev.sdb.client.view.desktop.detail.SoundtrackDetailWidget;
import dev.sdb.shared.model.entity.Entity;
import dev.sdb.shared.model.entity.Music;
import dev.sdb.shared.model.entity.Release;
import dev.sdb.shared.model.entity.Series;
import dev.sdb.shared.model.entity.Soundtrack;

public class UiFactoryImpl implements UiFactory {

	private static class RowColumConfig {
		private final Column<?, ?> column;
		private final double width;

		private RowColumConfig(Column<?, ?> column, double width) {
			super();
			this.column = column;
			this.width = width;
		}

		private Column<?, ?> getPushColumn() {
			return this.column;
		}

		private double getTotalWidth() {
			return this.width;
		}
	}

	private static abstract class BrowseConfig {

		private ContentPresenterType type;

		private BrowseConfig(ContentPresenterType type) {
			super();
			this.type = type;
		}

		private ContentPresenterType getType() {
			return this.type;
		}

		protected abstract long getId(Entity entity);
	}

	public static String getBrowserPrefixedCssDefinition(String attribute, String value) {
		String all = "";
		all += "-webkit-" + attribute + ": " + value + "; ";
		all += "-moz-" + attribute + ": " + value + "; ";
		all += "-o-" + attribute + ": " + value + "; ";
		all += attribute + ": " + value + ";";
		return all;
	}

	private static final double COLUMN_WIDTH_GOTO = 40.0;
	private static final double COLUMN_WIDTH_ID = 80.0;
	private static final double COLUMN_WIDTH_COMPACT_CELL = 500.0;
	private static final double COLUMN_WIDTH_COMPACT_CELL_MAX = 1400.0;

	private NavigatorView navigatorView;
	private HeaderView headerView;
	private FooterView footerView;
	private SectionInfoView sectionInfoView;

	private ErrorView errorView;

	private HomeView homeView;

	private ReleaseQueryView releaseQueryView;
	private MusicQueryView musicQueryView;
	private SoundtrackQueryView soundtrackQueryView;
	private SeriesQueryView seriesQueryView;
	private CatalogTreeView catalogTreeView;

	private ReleaseDetailView releaseDetailView;
	private MusicDetailView musicDetailView;
	private SoundtrackDetailView soundtrackDetailView;
	private SeriesDetailView seriesDetailView;
	private CatalogDetailView catalogDetailView;

	private ClientFactory clientFactory;

	public UiFactoryImpl() {
		super();
	}

	@Override public void setClientFactory(ClientFactory clientFactory) {
		this.clientFactory = clientFactory;
	}

	//	private boolean isSearchResultCompactView() {
	//		return true;
	//	}

	@Override public SectionInfoView getSectionInfoView() {
		if (this.sectionInfoView == null)
			this.sectionInfoView = new SectionInfoWidget();
		return this.sectionInfoView;
	}

	@Override public NavigatorView getNavigatorView() {
		if (this.navigatorView == null)
			this.navigatorView = new NavigatorWidget();
		return this.navigatorView;
	}

	@Override public HeaderView getHeaderView() {
		if (this.headerView == null)
			this.headerView = new HeaderWidget();
		return this.headerView;
	}

	@Override public FooterView getFooterView() {
		if (this.footerView == null)
			this.footerView = new FooterWidget();
		return this.footerView;
	}

	@Override public ErrorView getErrorView() {
		if (this.errorView == null)
			this.errorView = new ErrorWidget();
		return this.errorView;
	}
	@Override public HomeView getHomeView() {
		if (this.homeView == null)
			this.homeView = new HomeWidget();
		return this.homeView;
	}

	@Override public ReleaseQueryView getReleaseQueryView() {
		if (this.releaseQueryView == null) {
			this.releaseQueryView = new ReleaseQueryWidget();
			appendQueryReleaseColums(this.releaseQueryView.getResultTable());
		}
		return this.releaseQueryView;
	}

	@Override public MusicQueryView getMusicQueryView() {
		if (this.musicQueryView == null) {
			this.musicQueryView = new MusicQueryWidget();
			appendQueryMusicColums(this.musicQueryView.getResultTable());

		}
		return this.musicQueryView;
	}

	@Override public SoundtrackQueryView getSoundtrackQueryView() {
		if (this.soundtrackQueryView == null) {
			this.soundtrackQueryView = new SoundtrackQueryWidget();
			appendQuerySoundtrackColums(this.soundtrackQueryView.getResultTable());
		}
		return this.soundtrackQueryView;
	}

	private void setTableConfig(DataGrid<Entity> table, RowColumConfig config) {
		double minWidth = config.getTotalWidth() + COLUMN_WIDTH_GOTO;

		table.setWidth("100%");
		table.setTableWidth(100.0, Unit.PCT);
		table.setMinimumTableWidth(minWidth, Unit.PX);

		Column<?, ?> pushColumn = config.getPushColumn();
		if (pushColumn != null)
			table.getColumnSortList().push(pushColumn);
	}

	protected void appendSublistReleaseSoundtrackColumns(DataGrid<Entity> table) {
		addGotoColumn(table, new BrowseConfig(ContentPresenterType.MUSIC) {
			@Override protected long getId(Entity entity) {
				return ((Soundtrack) entity).getMusic().getId();
			}
		});
		RowColumConfig config = addCompactSublistReleaseSoundtrackColumns(table, COLUMN_WIDTH_COMPACT_CELL_MAX);

		setTableConfig(table, config);
	}

	protected void appendSublistSeriesReleaseColumns(DataGrid<Entity> table) {
		addGotoColumn(table, new BrowseConfig(ContentPresenterType.RELEASE) {
			@Override protected long getId(Entity entity) {
				return entity.getId();
			}
		});
		RowColumConfig config = addCompactReleaseColumns(table, COLUMN_WIDTH_COMPACT_CELL_MAX);

		setTableConfig(table, config);
	}

	protected void appendSublistMusicReleaseColumns(DataGrid<Entity> table) {
		addGotoColumn(table, new BrowseConfig(ContentPresenterType.RELEASE) {
			@Override protected long getId(Entity entity) {
				return entity.getId();
			}
		});
		RowColumConfig config = addCompactReleaseColumns(table, COLUMN_WIDTH_COMPACT_CELL_MAX);

		setTableConfig(table, config);
	}

	protected void appendSublistCatalogReleaseColumns(DataGrid<Entity> table) {
		addGotoColumn(table, new BrowseConfig(ContentPresenterType.RELEASE) {
			@Override protected long getId(Entity entity) {
				return entity.getId();
			}
		});
		RowColumConfig config = addCompactReleaseColumns(table, COLUMN_WIDTH_COMPACT_CELL_MAX);

		setTableConfig(table, config);
	}

	protected void appendQuerySeriesColums(DataGrid<Entity> table) {
		addGotoColumn(table, new BrowseConfig(ContentPresenterType.SERIES) {
			@Override protected long getId(Entity entity) {
				return entity.getId();
			}
		});
		RowColumConfig config = addCompactSeriesColumns(table, COLUMN_WIDTH_COMPACT_CELL_MAX);

		setTableConfig(table, config);
	}

	protected void appendQuerySoundtrackColums(DataGrid<Entity> table) {
		addGotoColumn(table, new BrowseConfig(ContentPresenterType.SOUNDTRACK) {
			@Override protected long getId(Entity entity) {
				return entity.getId();
			}
		});
		RowColumConfig config = addCompactSoundtrackColumns(table, COLUMN_WIDTH_COMPACT_CELL_MAX);

		setTableConfig(table, config);
	}

	protected void appendQueryReleaseColums(DataGrid<Entity> table) {
		addGotoColumn(table, new BrowseConfig(ContentPresenterType.RELEASE) {
			@Override protected long getId(Entity entity) {
				return entity.getId();
			}
		});
		RowColumConfig config = addCompactReleaseColumns(table, COLUMN_WIDTH_COMPACT_CELL_MAX);

		setTableConfig(table, config);
	}

	protected void appendQueryMusicColums(DataGrid<Entity> table) {
		addGotoColumn(table, new BrowseConfig(ContentPresenterType.MUSIC) {
			@Override protected long getId(Entity entity) {
				return entity.getId();
			}
		});
		RowColumConfig config = addCompactMusicColumns(table, COLUMN_WIDTH_COMPACT_CELL_MAX);

		setTableConfig(table, config);
	}

	protected void appendCatalogTreeReleaseColumns(DataGrid<Entity> table) {
		addGotoColumn(table, new BrowseConfig(ContentPresenterType.RELEASE) {
			@Override protected long getId(Entity entity) {
				return entity.getId();
			}
		});
		RowColumConfig config = addCompactReleaseColumns(table, COLUMN_WIDTH_COMPACT_CELL_MAX);

		setTableConfig(table, config);
	}

	@Override public SeriesQueryView getSeriesQueryView() {
		if (this.seriesQueryView == null) {
			this.seriesQueryView = new SeriesQueryWidget();
			appendQuerySeriesColums(this.seriesQueryView.getResultTable());
		}
		return this.seriesQueryView;
	}



	@Override public CatalogTreeView getCatalogTreeView(CatalogTreeView.Presenter presenter) {
		if (this.catalogTreeView == null) {
			this.catalogTreeView = new CatalogTreeWidget(presenter);
			appendCatalogTreeReleaseColumns(this.catalogTreeView.getReleaseTable());
		}
		return this.catalogTreeView;
	}

	@Override public ReleaseDetailView getReleaseDetailView() {
		if (this.releaseDetailView == null) {
			this.releaseDetailView = new ReleaseDetailWidget();
			appendSublistReleaseSoundtrackColumns(this.releaseDetailView.getSublistTable());
		}
		return this.releaseDetailView;
	}



	@Override public MusicDetailView getMusicDetailView() {
		if (this.musicDetailView == null) {
			this.musicDetailView = new MusicDetailWidget();
			appendSublistMusicReleaseColumns(this.musicDetailView.getSublistTable());
		}
		return this.musicDetailView;
	}

	@Override public SoundtrackDetailView getSoundtrackDetailView() {
		if (this.soundtrackDetailView == null) {
			this.soundtrackDetailView = new SoundtrackDetailWidget();
			//no sublist
		}
		return this.soundtrackDetailView;
	}

	@Override public SeriesDetailView getSeriesDetailView() {
		if (this.seriesDetailView == null) {
			this.seriesDetailView = new SeriesDetailWidget();
			appendSublistSeriesReleaseColumns(this.seriesDetailView.getSublistTable());
		}
		return this.seriesDetailView;
	}

	@Override public CatalogDetailView getCatalogDetailView() {
		if (this.catalogDetailView == null) {
			this.catalogDetailView = new CatalogDetailWidget();
			appendSublistCatalogReleaseColumns(this.catalogDetailView.getSublistTable());
		}
		return this.catalogDetailView;
	}

	private Column<Entity, SafeHtml> createCompactReleaseColumn() {
		final SafeHtmlCell releaseCell = new SafeHtmlCell();
		Column<Entity, SafeHtml> releaseColumn = new Column<Entity, SafeHtml>(releaseCell) {
			@Override public SafeHtml getValue(Entity entity) {
				if (entity == null)
					return null;

				Release release;
				if (entity instanceof Soundtrack)
					release = ((Soundtrack) entity).getRelease();
				else
					release = (Release) entity;

				String artworkUrl = URL.encode(release.getArtworkUrl());
				String title = release.getTitleInfo();
				String catalogInfo = release.getCatalogInfo();
				String yearInfo = release.getYearInfo();
				
				String era = release.getType() + (yearInfo == null || yearInfo.isEmpty() ? "" : (" von " + yearInfo));
				
				SafeHtmlBuilder sb = new SafeHtmlBuilder();

				//Open one-row-table 
				sb.appendHtmlConstant("<table style='padding: 0px; margin: 0px; border-spacing: 0px;'><tr>");

				//Release artwork cell
				sb.appendHtmlConstant("<td style='width: 50px; height: 50px; text-align: center;'>");
				sb.appendHtmlConstant("<img style='max-width: 50px; max-height: 50px;' src='" + artworkUrl + "'>");
				sb.appendHtmlConstant("</td>");

				//Release info cell
				sb.appendHtmlConstant("<td>");
				sb.appendHtmlConstant("<b>" + title + "</b>");
				sb.appendHtmlConstant("<br>");
				sb.appendHtmlConstant(catalogInfo);
				sb.appendHtmlConstant("<br>");
				sb.appendHtmlConstant("<i>" + era + "</i>");
				sb.appendHtmlConstant("</td>");

				//close one-row-table 
				sb.appendHtmlConstant("</tr></table>");

				return sb.toSafeHtml();
			}
		};
		return releaseColumn;
	}

	private Column<Entity, SafeHtml> createCompactMusicColumn() {
		final SafeHtmlCell musicCell = new SafeHtmlCell();
		Column<Entity, SafeHtml> musicColumn = new Column<Entity, SafeHtml>(musicCell) {
			@Override public SafeHtml getValue(Entity entity) {
				if (entity == null)
					return null;

				Music music;
				if (entity instanceof Soundtrack)
					music = ((Soundtrack) entity).getMusic();
				else
					music = (Music) entity;

				String artist = null;
				String authors = null;
				String title = null;

				if (music != null) {
					artist = music.getArtist();
					title = music.getTitleInfo();
					if (artist != null)
						artist += " (" + music.getYearInfo() + ")";
					authors = music.getAuthors();
				}

				SafeHtmlBuilder sb = new SafeHtmlBuilder();
				sb.appendHtmlConstant((artist == null) ? "&nbsp;" : artist);
				sb.appendHtmlConstant("<br>");
				sb.appendHtmlConstant((title == null) ? ("<i>keine Angabe</i>") : ("<b>" + title + "</b>"));
				sb.appendHtmlConstant("<br>");
				sb.appendHtmlConstant((authors == null) ? "&nbsp;" : ("<i>(" + authors + ")</i>"));
				return sb.toSafeHtml();
			}
		};
		return musicColumn;
	}

	private Column<Entity, SafeHtml> createCompactSeriesColumn() {
		final SafeHtmlCell seriesCell = new SafeHtmlCell();
		Column<Entity, SafeHtml> seriesColumn = new Column<Entity, SafeHtml>(seriesCell) {
			@Override public SafeHtml getValue(Entity entity) {
				if (entity == null)
					return null;

				Series series = (Series) entity;

				SafeHtmlBuilder sb = new SafeHtmlBuilder();
				sb.appendHtmlConstant("<b>" + series.getTitle() + "</b>");
				return sb.toSafeHtml();
			}
		};
		return seriesColumn;
	}



	private static final String IMG_STATIC_ROOT_URL = "http://localhost/mimg/static/";

	private Column<Entity, Entity> createGotoColumn(final BrowseConfig config) {


		ActionCell.Delegate<Entity> delegate = new ActionCell.Delegate<Entity>() {
			@Override public void execute(Entity entity) {
				String entityToken = config.getType().getToken() + "?id=" + config.getId(entity);
				UiFactoryImpl.this.clientFactory.getHistoryManager().createHistory(entityToken, true);
				//				Window.alert("You clicked " + entity.getMatch());
			}
		};

		SafeHtmlBuilder sb = new SafeHtmlBuilder();
		sb.appendHtmlConstant("<img width='16' height='16' src='" + IMG_STATIC_ROOT_URL + "goto16.png' alt='Gehe zu' title='Details anzeigen'>");

		ActionCell<Entity> gotoCell = new ActionCell<Entity>(sb.toSafeHtml(), delegate);

		Column<Entity, Entity> gotoColumn = new Column<Entity, Entity>(gotoCell) {
			@Override public Entity getValue(Entity entity) {
				return entity;
			}
		};

		gotoColumn.setCellStyleNames("gotoColumn");

		return gotoColumn;
	}

	private void addGotoColumn(DataGrid<Entity> table, BrowseConfig config) {
		Column<Entity, Entity> gotoColumn = createGotoColumn(config);
		// Make the columns non-sortable.
		gotoColumn.setSortable(false);

		table.addColumn(gotoColumn, "");
		table.setColumnWidth(gotoColumn, COLUMN_WIDTH_GOTO, Unit.PX);
	}

	private RowColumConfig addCompactSoundtrackColumns(DataGrid<Entity> table, double width) {
		TextColumn<Entity> soundtrackColumn = new TextColumn<Entity>() {
			@Override public String getValue(Entity entity) {
				return "#" + ((Soundtrack) entity).getId();
			}
		};
		// Make the columns sortable.
		soundtrackColumn.setSortable(true);

		// Add the column.
		table.addColumn(soundtrackColumn, "ID");
		table.setColumnWidth(soundtrackColumn, COLUMN_WIDTH_ID, Unit.PX);

		// Add the release column(s)
		RowColumConfig releaseConfig = addCompactReleaseColumns(table, COLUMN_WIDTH_COMPACT_CELL);

		// Add the music column(s)
		RowColumConfig musicConfig = addCompactMusicColumns(table, width);

		return new RowColumConfig(soundtrackColumn, COLUMN_WIDTH_ID + releaseConfig.getTotalWidth() + musicConfig.getTotalWidth());
	}

	@SuppressWarnings("unused") private RowColumConfig addExpandedSoundtrackColumns(DataGrid<Entity> table) {
		TextColumn<Entity> soundtrackColumn = new TextColumn<Entity>() {
			@Override public String getValue(Entity entity) {
				return "#" + ((Soundtrack) entity).getId();
			}
		};
		// Make the columns sortable.
		soundtrackColumn.setSortable(true);

		// Add the column.
		table.addColumn(soundtrackColumn, "ID");
		table.setColumnWidth(soundtrackColumn, COLUMN_WIDTH_ID, Unit.PX);

		// Add the release column(s)
		RowColumConfig releaseConfig = addExpandedReleaseColumns(table);

		// Add the music column(s)
		RowColumConfig musicConfig = addExpandedMusicColumns(table);

		return new RowColumConfig(soundtrackColumn, COLUMN_WIDTH_ID + releaseConfig.getTotalWidth() + musicConfig.getTotalWidth());
	}

	private RowColumConfig addCompactReleaseColumns(DataGrid<Entity> table, double width) {
		Column<Entity, SafeHtml> releaseColumn = createCompactReleaseColumn();
		// Make the columns sortable.
		releaseColumn.setSortable(true);

		table.addColumn(releaseColumn, "Veröffentlichung");
		table.setColumnWidth(releaseColumn, width, Unit.PX);

		return new RowColumConfig(releaseColumn, width);
	}

	private RowColumConfig addExpandedReleaseColumns(DataGrid<Entity> table) {
		TextColumn<Entity> typeColumn = new TextColumn<Entity>() {
			@Override public String getValue(Entity entity) {
				return ((Release) entity).getType();
			}
		};

		TextColumn<Entity> catColumn = new TextColumn<Entity>() {
			@Override public String getValue(Entity entity) {
				return ((Release) entity).getCatalogInfo();
			}
		};

		TextColumn<Entity> yearColumn = new TextColumn<Entity>() {
			@Override public String getValue(Entity entity) {
				return ((Release) entity).getYearInfo();
			}
		};

		TextColumn<Entity> titleColumn = new TextColumn<Entity>() {
			@Override public String getValue(Entity entity) {
				return ((Release) entity).getTitleInfo();
			}
		};

		//		 Make the columns sortable.
		titleColumn.setSortable(true);

		// Add the columns.
		table.addColumn(typeColumn, "Typ");
		table.setColumnWidth(typeColumn, 50.0, Unit.PX);

		table.addColumn(catColumn, "Kat.-Nr.");
		table.setColumnWidth(catColumn, 150.0, Unit.PX);

		table.addColumn(yearColumn, "Jahr");
		table.setColumnWidth(yearColumn, 50.0, Unit.PX);

		table.addColumn(titleColumn, "Titel");
		table.setColumnWidth(titleColumn, 150.0, Unit.PX);

		return new RowColumConfig(titleColumn, 400.0);
	}

	private RowColumConfig addCompactMusicColumns(DataGrid<Entity> table, double width) {
		Column<Entity, SafeHtml> musicColumn = createCompactMusicColumn();

		musicColumn.setSortable(true); // Make the column sortable.

		table.addColumn(musicColumn, "Musik");
		table.setColumnWidth(musicColumn, width, Unit.PX);

		return new RowColumConfig(musicColumn, width);
	}

	private RowColumConfig addExpandedMusicColumns(DataGrid<Entity> table) {
		TextColumn<Entity> genreColumn = new TextColumn<Entity>() {
			@Override public String getValue(Entity entity) {
				return ((Music) entity).getGenre().getChildName();
			}
		};

		TextColumn<Entity> yearColumn = new TextColumn<Entity>() {
			@Override public String getValue(Entity entity) {
				return "" + ((Music) entity).getYear();
			}
		};

		TextColumn<Entity> artistColumn = new TextColumn<Entity>() {
			@Override public String getValue(Entity entity) {
				return ((Music) entity).getArtist();
			}
		};

		TextColumn<Entity> titleColumn = new TextColumn<Entity>() {
			@Override public String getValue(Entity entity) {
				return ((Music) entity).getTitleInfo();
			}
		};

		// Make the columns sortable.
		titleColumn.setSortable(true);

		// Add the columns.
		table.addColumn(genreColumn, "Genre");
		table.setColumnWidth(genreColumn, 50.0, Unit.PX);

		table.addColumn(yearColumn, "Jahr");
		table.setColumnWidth(yearColumn, 50.0, Unit.PX);

		table.addColumn(artistColumn, "Interpret");
		table.setColumnWidth(artistColumn, 150.0, Unit.PX);

		table.addColumn(titleColumn, "Titel");
		table.setColumnWidth(titleColumn, 200.0, Unit.PX);

		return new RowColumConfig(titleColumn, 450.0);
	}

	private RowColumConfig addCompactSublistReleaseSoundtrackColumns(DataGrid<Entity> table, double width) {
		TextColumn<Entity> soundtrackColumn = new TextColumn<Entity>() {
			@Override public String getValue(Entity entity) {
				return "#" + ((Soundtrack) entity).getId();
			}
		};
		// Make the columns sortable.
		soundtrackColumn.setSortable(true);

		// Add the column.
		table.addColumn(soundtrackColumn, "ID");
		table.setColumnWidth(soundtrackColumn, COLUMN_WIDTH_ID, Unit.PX);

		// Add the music column(s)
		RowColumConfig musicConfig = addCompactMusicColumns(table, width);

		return new RowColumConfig(soundtrackColumn, COLUMN_WIDTH_ID + musicConfig.getTotalWidth());
	}

	@SuppressWarnings("unused") private RowColumConfig addExpandedSublistReleaseSoundtrackColumns(DataGrid<Entity> table) {
		TextColumn<Entity> soundtrackColumn = new TextColumn<Entity>() {
			@Override public String getValue(Entity entity) {
				return "#" + ((Soundtrack) entity).getId();
			}
		};
		// Make the columns sortable.
		soundtrackColumn.setSortable(true);

		// Add the column.
		table.addColumn(soundtrackColumn, "ID");
		table.setColumnWidth(soundtrackColumn, COLUMN_WIDTH_ID, Unit.PX);

		// Add the music column(s)
		RowColumConfig musicConfig = addExpandedMusicColumns(table);

		return new RowColumConfig(soundtrackColumn, COLUMN_WIDTH_ID + musicConfig.getTotalWidth());
	}

	private RowColumConfig addCompactSeriesColumns(DataGrid<Entity> table, double width) {
		Column<Entity, SafeHtml> seriesColumn = createCompactSeriesColumn();
		// Make the columns sortable.
		seriesColumn.setSortable(true);

		table.addColumn(seriesColumn, "Serien");
		table.setColumnWidth(seriesColumn, width, Unit.PX);

		return new RowColumConfig(seriesColumn, width);
	}

	@SuppressWarnings("unused") private RowColumConfig addExpandedSeriesColumns(DataGrid<Entity> table) {
		TextColumn<Entity> titleColumn = new TextColumn<Entity>() {
			@Override public String getValue(Entity entity) {
				return ((Series) entity).getTitle();
			}
		};

		// Make the columns sortable.
		titleColumn.setSortable(true);

		table.addColumn(titleColumn, "Titel");
		table.setColumnWidth(titleColumn, 200.0, Unit.PX);

		return new RowColumConfig(titleColumn, 200.0);
	}
}

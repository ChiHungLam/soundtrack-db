package dev.sdb.client.view.desktop;

import java.util.List;
import java.util.Vector;

import com.google.gwt.cell.client.ActionCell;
import com.google.gwt.cell.client.SafeHtmlCell;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.http.client.URL;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;

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
import dev.sdb.shared.model.db.Flavor;
import dev.sdb.shared.model.entity.Entity;
import dev.sdb.shared.model.entity.Music;
import dev.sdb.shared.model.entity.Release;
import dev.sdb.shared.model.entity.Series;
import dev.sdb.shared.model.entity.Soundtrack;

public class UiFactoryImpl implements UiFactory {

	private static class ColumConfig {

		private final Column<Entity, ?> column;
		private final String title;
		private final double width;
		private final boolean sortable;

		private ColumConfig(Column<Entity, ?> column, String title, double width, boolean sortable) {
			super();
			this.column = column;
			this.title = title;
			this.width = width;
			this.sortable = sortable;
		}

		private Column<Entity, ?> getColumn() {
			return this.column;
		}
		private String getTitle() {
			return this.title;
		}
		private double getWidth() {
			return this.width;
		}
		private boolean isSortable() {
			return this.sortable;
		}
	}

	private static class ColumSetConfig {
		private List<ColumConfig> columns;
		private Column<Entity, ?> pushColumn;

		private ColumSetConfig() {
			super();
			this.columns = new Vector<ColumConfig>();
			this.pushColumn = null;
		}

		private Column<Entity, ?> addColumn(Column<Entity, ?> column, String title, double width, boolean sortable) {
			if (column == null)
				return null;

			this.columns.add(new ColumConfig(column, title, width, sortable));
			return column;
		}

		private void setPushColumn(Column<Entity, ?> pushColumn) {
			this.pushColumn = pushColumn;
		}

		private Column<Entity, ?> getPushColumn() {
			return this.pushColumn;
		}

		private List<ColumConfig> getColumns() {
			return this.columns;
		}
	}

	private static abstract class BrowseConfig {

		private ContentPresenterType type;
		private Flavor flavor;

		private BrowseConfig(ContentPresenterType type, Flavor flavor) {
			super();
			this.type = type;
			this.flavor = flavor;
		}

		protected ContentPresenterType getType() {
			return this.type;
		}

		protected Flavor getFlavor() {
			return this.flavor;
		}

		protected abstract long getId(Entity entity);
	}

	private class HtmlFactory implements UiFactory.HtmlFactory {

		private static final String IMG_STATIC_ROOT_URL = "http://localhost/mimg/static/";

		@Override public SafeHtml getMusicInfoCompact(Music music) {

			String artist = null;
			String authors = null;
			String title = null;

			if (music != null) {
				artist = music.getArtist();
				title = music.getTitleInfo();
				if (artist != null)
					artist += " (" + music.getYearInfo() + ")";
				authors = music.getAuthors();
				if (authors == null && artist != null)
					authors = "Komponist unbekannt";
			}

			SafeHtmlBuilder sb = new SafeHtmlBuilder();

			//Open one-row-table 
			sb.appendHtmlConstant("<table style='padding: 0px; margin: 0px; border-spacing: 0px;'><tr>");

			//Music info cell
			sb.appendHtmlConstant("<td style='padding-left: 5px; text-align: left;'>");
			sb.appendHtmlConstant((artist == null) ? "&nbsp;" : artist);
			sb.appendHtmlConstant("<br>");
			sb.appendHtmlConstant((title == null) ? ("<i>keine Angabe</i>") : ("<b>" + title + "</b>"));
			sb.appendHtmlConstant("<br>");
			sb.appendHtmlConstant((authors == null) ? "&nbsp;" : ("<i>(" + authors + ")</i>"));
			sb.appendHtmlConstant("</td>");

			//close one-row-table 
			sb.appendHtmlConstant("</tr></table>");

			return sb.toSafeHtml();
		}

		@Override public SafeHtml getSeriesInfo(Series series) {

			SafeHtmlBuilder sb = new SafeHtmlBuilder();
			sb.appendHtmlConstant("<div style='text-align: left;'>");
			sb.appendHtmlConstant("<b>" + series.getTitle() + "</b>");
			sb.appendHtmlConstant("</div>");

			return sb.toSafeHtml();
		}

		@Override public SafeHtml getReleaseInfoCompact(Release release) {

			String artworkUrl = URL.encode(release.getArtworkUrl());
			String title = release.getTitleInfo();
			String catalogInfo = release.getCatalogInfo();
			String yearInfo = release.getYearInfo();

			String era = release.getType() + (yearInfo == null || yearInfo.isEmpty() ? "" : (" von " + yearInfo));

			String sizePx = "50px";

			SafeHtmlBuilder sb = new SafeHtmlBuilder();

			//Open one-row-table 
			sb.appendHtmlConstant("<table style='padding: 0px; margin: 0px; border-spacing: 0px;'><tr>");

			//Release artwork cell
			sb.appendHtmlConstant("<td style='width: " + sizePx + "; height: " + sizePx + "; text-align: center;'>");
			sb.appendHtmlConstant("<img style='max-width: " + sizePx + "; max-height: " + sizePx + ";' src='" + artworkUrl + "'>");
			sb.appendHtmlConstant("</td>");

			//Release info cell
			sb.appendHtmlConstant("<td style='padding-left: 5px; text-align: left;'>");
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

		@Override public SafeHtml getMusicInfoDetailed(Music music) {

			String artist = null;
			String authors = null;
			String title = null;
			String duration = null;

			if (music != null) {
				artist = music.getArtist();
				title = music.getTitleInfo();
				if (artist != null)
					artist += " (" + music.getYearInfo() + ")";
				authors = music.getAuthors();
				if (authors == null && artist != null)
					authors = "Komponist unbekannt";

				int durationSeconds = music.getDurationSeconds();
				duration = (durationSeconds <= 0) ? null : formatTime(durationSeconds);
			}


			SafeHtmlBuilder sb = new SafeHtmlBuilder();

			//Open one-row-table 
			sb.appendHtmlConstant("<table style='padding: 0px; margin: 0px; border-spacing: 0px;'><tr>");

			//Music info cell
			sb.appendHtmlConstant("<td style='padding-left: 5px; text-align: left; vertical-align: top;'>");
			sb.appendHtmlConstant("<span style='font-size:10pt;'>");
			sb.appendHtmlConstant((artist == null) ? "&nbsp;" : artist);
			sb.appendHtmlConstant("<br>");
			sb.appendHtmlConstant((title == null) ? ("<i>keine Angabe</i>") : ("<span style='font-size:14pt;'><b>" + title + "</b></span>"));
			sb.appendHtmlConstant("<br>");
			sb.appendHtmlConstant((authors == null) ? "&nbsp;" : ("<i>(" + authors + ")</i>"));
			sb.appendHtmlConstant("<br><br>");
			sb.appendHtmlConstant("Spielzeit: " + (duration == null ? "unbekannt" : (" ca. " + duration + " min.")));
			sb.appendHtmlConstant("</span>");
			sb.appendHtmlConstant("</td>");

			//close one-row-table 
			sb.appendHtmlConstant("</tr></table>");

			return sb.toSafeHtml();
		}

		@Override public SafeHtml getReleaseInfoDetailed(Release release) {

			String artworkUrl = URL.encode(release.getArtworkUrl());
			String title = release.getTitleInfo();
			String catalogInfo = release.getCatalogInfo();
			String yearInfo = release.getYearInfo();
			int durationSeconds = release.getDurationSeconds();

			String era = release.getType() + (yearInfo == null || yearInfo.isEmpty() ? "" : (" von " + yearInfo));

			String duration = (durationSeconds <= 0) ? null : formatTime(durationSeconds);

			String sizePx = "100px";

			SafeHtmlBuilder sb = new SafeHtmlBuilder();

			//Open one-row-table 
			sb.appendHtmlConstant("<table style='padding: 0px; margin: 0px; border-spacing: 0px;'><tr>");

			//Release artwork cell
			sb.appendHtmlConstant("<td style='width: " + sizePx + "; height: " + sizePx + "; padding: 0px; text-align: center;'>");
			sb.appendHtmlConstant("<img style='max-width: " + sizePx + "; max-height: " + sizePx + ";' src='" + artworkUrl + "'>");
			sb.appendHtmlConstant("</td>");

			//Release info cell
			sb.appendHtmlConstant("<td style='padding: 0px; padding-left: 10px; text-align: left; vertical-align: top;'>");
			sb.appendHtmlConstant("<span style='font-size:14pt;'><b>" + title + "</b></span>");
			sb.appendHtmlConstant("<br>");
			sb.appendHtmlConstant("<span style='font-size:10pt;'>");
			sb.appendHtmlConstant(catalogInfo);
			sb.appendHtmlConstant("<br>");
			sb.appendHtmlConstant("<i>" + era + "</i>");
			sb.appendHtmlConstant("<br><br>");
			sb.appendHtmlConstant("Spielzeit: " + (duration == null ? "unbekannt" : (" ca. " + duration + " min.")));
			sb.appendHtmlConstant("</span>");
			sb.appendHtmlConstant("</td>");

			//close one-row-table 
			sb.appendHtmlConstant("</tr></table>");

			return sb.toSafeHtml();
		}

		@Override public SafeHtml getSoundtrackTime(Soundtrack soundtrack) {

			String time = formatSoundtrackTime(soundtrack);

			SafeHtmlBuilder sb = new SafeHtmlBuilder();
			sb.appendHtmlConstant((time == null) ? "<i>k.A.</i>" : "" + time + "");

			return sb.toSafeHtml();
		}

		@Override public SafeHtml getSoundtrackSeqNum(Soundtrack soundtrack) {

			String seqColor = getSequenceColor(soundtrack.getMediaIndex());

			SafeHtmlBuilder sb = new SafeHtmlBuilder();
			sb.appendHtmlConstant("<div style='" +
					"position: absolute;" +
					"width: 100%;" +
					"height: 100%;" +
					"top: 0px;" +
					seqColor +
					"text-align: center;" +
					"'>&nbsp;<br><b>" + soundtrack.getSeqNum() + "</b></div>");

			return sb.toSafeHtml();
		}

		protected SafeHtml getGotoImage(Flavor flavor) {

			String title = flavor.getPrefix() + "-Details anzeigen";

			SafeHtmlBuilder sb = new SafeHtmlBuilder();
			sb.appendHtmlConstant("<img style='margin-top: 4px; width: 16px; height: 16px;' src='" + IMG_STATIC_ROOT_URL + "goto16.png' alt='Gehe zu' title='" + title + "'>");

			return sb.toSafeHtml();
		}

		public SafeHtml getIdSpan(Entity entity) {

			String title = entity.getFlavor().getPrefix() + "-ID: #" + entity.getId();

			SafeHtmlBuilder sb = new SafeHtmlBuilder();
			sb.appendHtmlConstant("<span title='" + title + "'>#" + entity.getId() + "</span>");

			return sb.toSafeHtml();
		}

		private String formatTime(int time) {
			if (time == 0)
				return "00:00";

			String prefix = (time < 0) ? "(-)" : "";

			long minute = time / 60;
			long second = time - (minute * 60);

			return prefix + ((minute < 10) ? "0" : "") + minute + ":" + ((second < 10) ? "0" : "") + second;
		}

		private String formatSoundtrackTime(Soundtrack soundtrack) {
			int start = soundtrack.getStartTime();
			int stop = soundtrack.getStopTime();
			if (start == 0 && stop == 0)
				return null;

			String time = formatTime(start) + "&nbsp;-&nbsp;" + formatTime(stop);

			return time;
		}

		private String getSequenceColor(int mediaIndex) {
			if (mediaIndex == 0) {
				return "background-color: red;";
			}
			if (mediaIndex == -1) {
				return "background-color: darkgray;";
			}
			if ((mediaIndex & 1) != 0) {
				return "background-color: cadetblue;";
			}
			if ((mediaIndex & 1) == 0) {
				return "background-color: burlywood;";
			}
			return "";
		}
	}

	private class ColumnFactory {

		private static final double COLUMN_WIDTH_GOTO = 40.0;
		private static final double COLUMN_WIDTH_ID = 80.0;

		protected void appendSublistReleaseSoundtrackColumns(CellTable<Entity> table) {
			ColumSetConfig config = new ColumSetConfig();
			addGotoColumn(table, config, new BrowseConfig(ContentPresenterType.MUSIC, Flavor.MUSIC) {
				@Override protected long getId(Entity entity) {
					return ((Soundtrack) entity).getMusic().getId();
				}
			});
			addSublistReleaseSoundtrackColumns(table, config);

			setTableConfig(table, config);
		}

		protected void appendSublistSeriesReleaseColumns(CellTable<Entity> table) {
			ColumSetConfig config = new ColumSetConfig();
			addGotoColumn(table, config, new BrowseConfig(ContentPresenterType.RELEASE, Flavor.RELEASE) {
				@Override protected long getId(Entity entity) {
					return entity.getId();
				}
			});
			config.addColumn(createIdColumn(), "ID", COLUMN_WIDTH_ID, true);
			addReleaseColumns(table, config);

			setTableConfig(table, config);
		}

		protected void appendSublistMusicReleaseColumns(CellTable<Entity> table) {
			ColumSetConfig config = new ColumSetConfig();
			addGotoColumn(table, config, new BrowseConfig(ContentPresenterType.RELEASE, Flavor.RELEASE) {
				@Override protected long getId(Entity entity) {
					return entity.getId();
				}
			});
			config.addColumn(createIdColumn(), "ID", COLUMN_WIDTH_ID, true);
			addReleaseColumns(table, config);

			setTableConfig(table, config);
		}

		protected void appendSublistCatalogReleaseColumns(CellTable<Entity> table) {
			ColumSetConfig config = new ColumSetConfig();
			addGotoColumn(table, config, new BrowseConfig(ContentPresenterType.RELEASE, Flavor.RELEASE) {
				@Override protected long getId(Entity entity) {
					return entity.getId();
				}
			});
			addReleaseColumns(table, config);

			setTableConfig(table, config);
		}

		protected void appendQuerySeriesColums(CellTable<Entity> table) {
			ColumSetConfig config = new ColumSetConfig();
			addGotoColumn(table, config, new BrowseConfig(ContentPresenterType.SERIES, Flavor.SERIES) {
				@Override protected long getId(Entity entity) {
					return entity.getId();
				}
			});
			config.addColumn(createIdColumn(), "ID", COLUMN_WIDTH_ID, true);
			addSeriesColumns(table, config);

			setTableConfig(table, config);
		}

		protected void appendQuerySoundtrackColums(CellTable<Entity> table) {
			ColumSetConfig config = new ColumSetConfig();
			addGotoColumn(table, config, new BrowseConfig(ContentPresenterType.SOUNDTRACK, Flavor.SOUNDTRACK) {
				@Override protected long getId(Entity entity) {
					return entity.getId();
				}
			});
			config.addColumn(createIdColumn(), "ID", COLUMN_WIDTH_ID, true);
			addSoundtrackColumns(table, config);

			setTableConfig(table, config);
		}

		protected void appendQueryReleaseColums(CellTable<Entity> table) {
			ColumSetConfig config = new ColumSetConfig();
			addGotoColumn(table, config, new BrowseConfig(ContentPresenterType.RELEASE, Flavor.RELEASE) {
				@Override protected long getId(Entity entity) {
					return entity.getId();
				}
			});
			config.addColumn(createIdColumn(), "ID", COLUMN_WIDTH_ID, true);
			addReleaseColumns(table, config);

			setTableConfig(table, config);
		}

		protected void appendQueryMusicColums(CellTable<Entity> table) {
			ColumSetConfig config = new ColumSetConfig();
			addGotoColumn(table, config, new BrowseConfig(ContentPresenterType.MUSIC, Flavor.MUSIC) {
				@Override protected long getId(Entity entity) {
					return entity.getId();
				}
			});
			config.addColumn(createIdColumn(), "ID", COLUMN_WIDTH_ID, true);
			addMusicColumns(table, config);

			setTableConfig(table, config);
		}

		protected void appendCatalogTreeReleaseColumns(CellTable<Entity> table) {
			ColumSetConfig config = new ColumSetConfig();
			addGotoColumn(table, config, new BrowseConfig(ContentPresenterType.RELEASE, Flavor.RELEASE) {
				@Override protected long getId(Entity entity) {
					return entity.getId();
				}
			});
			config.addColumn(createIdColumn(), "ID", COLUMN_WIDTH_ID, true);
			addReleaseColumns(table, config);

			setTableConfig(table, config);
		}

		private void setTableConfig(CellTable<Entity> table, ColumSetConfig config) {
			List<ColumConfig> columns = config.getColumns();
			Column<Entity, ?> pushColumn = config.getPushColumn();
			boolean tableSortable = (pushColumn != null);

			for (ColumConfig colConfig : columns) {
				Column<Entity, ?> col = colConfig.getColumn();
				String title = colConfig.getTitle();
				double width = colConfig.getWidth();
				boolean sortable = colConfig.isSortable();

				col.setSortable(sortable && tableSortable);
				table.addColumn(col, title);// Add the column.

				if (width >= 0)
					table.setColumnWidth(col, width, Unit.PX);
			}

			table.setWidth("100%", true);

			if (tableSortable)
				table.getColumnSortList().push(pushColumn);
		}

		private Column<Entity, ?> createGotoColumn(final BrowseConfig config) {
			ActionCell.Delegate<Entity> delegate = new ActionCell.Delegate<Entity>() {
				@Override public void execute(Entity entity) {
					String entityToken = config.getType().getToken() + "?id=" + config.getId(entity);
					UiFactoryImpl.this.clientFactory.getHistoryManager().createHistory(entityToken, true);
				}
			};

			
			SafeHtml imageHtml = UiFactoryImpl.this.htmlFactory.getGotoImage(config.getFlavor());

			ActionCell<Entity> cell = new ActionCell<Entity>(imageHtml, delegate);

			Column<Entity, Entity> column = new Column<Entity, Entity>(cell) {
				@Override public Entity getValue(Entity entity) {
					return entity;
				}
			};

			column.setCellStyleNames("gotoColumn");

			return column;
		}

		private Column<Entity, ?> createIdColumn() {
			if (!SHOW_ID_UI)
				return null;
			
			final SafeHtmlCell cell = new SafeHtmlCell();
			Column<Entity, SafeHtml> column = new Column<Entity, SafeHtml>(cell) {
				@Override public SafeHtml getValue(Entity entity) {
					if (entity == null)
						return null;

					return UiFactoryImpl.this.htmlFactory.getIdSpan(entity);
				}
			};
			return column;
		}

		private Column<Entity, ?> createReleaseColumn() {
			final SafeHtmlCell cell = new SafeHtmlCell();
			Column<Entity, SafeHtml> column = new Column<Entity, SafeHtml>(cell) {
				@Override public SafeHtml getValue(Entity entity) {
					if (entity == null)
						return null;

					Release release;
					if (entity instanceof Soundtrack)
						release = ((Soundtrack) entity).getRelease();
					else
						release = (Release) entity;

					return UiFactoryImpl.this.htmlFactory.getReleaseInfoCompact(release);
				}
			};
			return column;
		}

		private Column<Entity, ?> createMusicColumn() {
			final SafeHtmlCell cell = new SafeHtmlCell();
			Column<Entity, SafeHtml> column = new Column<Entity, SafeHtml>(cell) {
				@Override public SafeHtml getValue(Entity entity) {
					if (entity == null)
						return null;

					Music music;
					if (entity instanceof Soundtrack)
						music = ((Soundtrack) entity).getMusic();
					else
						music = (Music) entity;

					return UiFactoryImpl.this.htmlFactory.getMusicInfoCompact(music);
				}
			};
			return column;
		}

		private Column<Entity, ?> createSeriesColumn() {
			final SafeHtmlCell cell = new SafeHtmlCell();
			Column<Entity, SafeHtml> column = new Column<Entity, SafeHtml>(cell) {
				@Override public SafeHtml getValue(Entity entity) {
					if (entity == null)
						return null;

					Series series = (Series) entity;

					return UiFactoryImpl.this.htmlFactory.getSeriesInfo(series);
				}
			};
			return column;
		}

		private Column<Entity, ?> createSoundtrackSeqNumColumn() {
			final SafeHtmlCell cell = new SafeHtmlCell();
			Column<Entity, SafeHtml> column = new Column<Entity, SafeHtml>(cell) {
				@Override public SafeHtml getValue(Entity entity) {
					if (entity == null)
						return null;

					Soundtrack soundtrack = (Soundtrack) entity;

					return UiFactoryImpl.this.htmlFactory.getSoundtrackSeqNum(soundtrack);
				}
			};

			column.setCellStyleNames("sequenceColumn");

			return column;
		}

		private Column<Entity, ?> createSoundtrackTimeColumn() {
			final SafeHtmlCell cell = new SafeHtmlCell();
			Column<Entity, SafeHtml> column = new Column<Entity, SafeHtml>(cell) {
				@Override public SafeHtml getValue(Entity entity) {
					if (entity == null)
						return null;

					Soundtrack soundtrack = (Soundtrack) entity;

					return UiFactoryImpl.this.htmlFactory.getSoundtrackTime(soundtrack);
				}
			};
			return column;
		}

		private void addGotoColumn(CellTable<Entity> table, ColumSetConfig config, BrowseConfig browseConfig) {
			config.addColumn(createGotoColumn(browseConfig), "", COLUMN_WIDTH_GOTO, false);
		}

		private void addReleaseColumns(CellTable<Entity> table, ColumSetConfig config) {
			Column<Entity, ?> releaseColumn = config.addColumn(createReleaseColumn(), "Veröffentlichung", -1, true);
			config.setPushColumn(releaseColumn);
		}

		private void addMusicColumns(CellTable<Entity> table, ColumSetConfig config) {
			Column<Entity, ?> musicColumn = config.addColumn(createMusicColumn(), "Musik", -1, true);
			config.setPushColumn(musicColumn);
		}

		private void addSeriesColumns(CellTable<Entity> table, ColumSetConfig config) {
			Column<Entity, ?> seriesColumn = config.addColumn(createSeriesColumn(), "Serien", -1, true);
			config.setPushColumn(seriesColumn);
		}

		private void addSoundtrackColumns(CellTable<Entity> table, ColumSetConfig config) {
			addReleaseColumns(table, config); // Add the release column(s)

			Column<Entity, ?> soundtrackColumn = config.addColumn(createSoundtrackSeqNumColumn(), "Sequenz", 80.0, false);

			addMusicColumns(table, config); // Add the music column(s)
			config.addColumn(createSoundtrackTimeColumn(), "Zeit", 110.0, false);

			config.setPushColumn(soundtrackColumn);
		}

		private void addSublistReleaseSoundtrackColumns(CellTable<Entity> table, ColumSetConfig config) {
			//			config.addColumn(createSoundtrackIdColumn(), "ID", COLUMN_WIDTH_ID, false);
			config.addColumn(createSoundtrackSeqNumColumn(), "Sequenz", 80.0, false);

			addMusicColumns(table, config); // Add the music column(s)

			config.addColumn(createSoundtrackTimeColumn(), "Zeit", 110.0, false);

			config.setPushColumn(null);
		}
	}

	private static final boolean SHOW_ID_UI;

	static {
		SHOW_ID_UI = false;
	}

	public static String getBrowserPrefixedCssDefinition(String attribute, String value) {
		String all = "";
		all += "-webkit-" + attribute + ": " + value + "; ";
		all += "-moz-" + attribute + ": " + value + "; ";
		all += "-o-" + attribute + ": " + value + "; ";
		all += attribute + ": " + value + ";";
		return all;
	}

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
	private HtmlFactory htmlFactory;
	private ColumnFactory columnFactory;

	public UiFactoryImpl() {
		super();
		this.htmlFactory = new HtmlFactory();
		this.columnFactory = new ColumnFactory();
	}

	@Override public dev.sdb.client.view.UiFactory.HtmlFactory getHtmlFactory() {
		return this.htmlFactory;
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
			this.columnFactory.appendQueryReleaseColums(this.releaseQueryView.getResultTable());
		}
		return this.releaseQueryView;
	}

	@Override public MusicQueryView getMusicQueryView() {
		if (this.musicQueryView == null) {
			this.musicQueryView = new MusicQueryWidget();
			this.columnFactory.appendQueryMusicColums(this.musicQueryView.getResultTable());

		}
		return this.musicQueryView;
	}

	@Override public SoundtrackQueryView getSoundtrackQueryView() {
		if (this.soundtrackQueryView == null) {
			this.soundtrackQueryView = new SoundtrackQueryWidget();
			this.columnFactory.appendQuerySoundtrackColums(this.soundtrackQueryView.getResultTable());
		}
		return this.soundtrackQueryView;
	}

	@Override public SeriesQueryView getSeriesQueryView() {
		if (this.seriesQueryView == null) {
			this.seriesQueryView = new SeriesQueryWidget();
			this.columnFactory.appendQuerySeriesColums(this.seriesQueryView.getResultTable());
		}
		return this.seriesQueryView;
	}

	@Override public CatalogTreeView getCatalogTreeView(CatalogTreeView.Presenter presenter) {
		if (this.catalogTreeView == null) {
			this.catalogTreeView = new CatalogTreeWidget(presenter);
			this.columnFactory.appendCatalogTreeReleaseColumns(this.catalogTreeView.getReleaseTable());
		}
		return this.catalogTreeView;
	}

	@Override public ReleaseDetailView getReleaseDetailView() {
		if (this.releaseDetailView == null) {
			this.releaseDetailView = new ReleaseDetailWidget();
			this.columnFactory.appendSublistReleaseSoundtrackColumns(this.releaseDetailView.getSublistTable());
		}
		return this.releaseDetailView;
	}

	@Override public MusicDetailView getMusicDetailView() {
		if (this.musicDetailView == null) {
			this.musicDetailView = new MusicDetailWidget();
			this.columnFactory.appendSublistMusicReleaseColumns(this.musicDetailView.getSublistTable());
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
			this.columnFactory.appendSublistSeriesReleaseColumns(this.seriesDetailView.getSublistTable());
		}
		return this.seriesDetailView;
	}

	@Override public CatalogDetailView getCatalogDetailView() {
		if (this.catalogDetailView == null) {
			this.catalogDetailView = new CatalogDetailWidget();
			this.columnFactory.appendSublistCatalogReleaseColumns(this.catalogDetailView.getSublistTable());
		}
		return this.catalogDetailView;
	}

}

package dev.sdb.client.view.desktop;

import com.google.gwt.cell.client.SafeHtmlCell;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasEnabled;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

import dev.sdb.client.view.HomeView;
import dev.sdb.client.view.MusicDetailView;
import dev.sdb.client.view.MusicQueryView;
import dev.sdb.client.view.NavigatorView;
import dev.sdb.client.view.ReleaseDetailView;
import dev.sdb.client.view.ReleaseQueryView;
import dev.sdb.client.view.SeriesDetailView;
import dev.sdb.client.view.SeriesQueryView;
import dev.sdb.client.view.SoundtrackDetailView;
import dev.sdb.client.view.SoundtrackQueryView;
import dev.sdb.client.view.UiFactory;
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
	/**
	 * The message displayed to the user when the server cannot be reached or returns an error.
	 */
	public static final String SERVER_ERROR = "An error occurred while "
			+ "attempting to contact the server. Please check your network "
			+ "connection and try again.";

	private NavigatorView navigatorView;
	private HomeView homeView;

	private ReleaseQueryView releaseQueryView;
	private MusicQueryView musicQueryView;
	private SoundtrackQueryView soundtrackQueryView;
	private SeriesQueryView seriesQueryView;

	private ReleaseDetailView releaseDetailView;
	private MusicDetailView musicDetailView;
	private SoundtrackDetailView soundtrackDetailView;
	private SeriesDetailView seriesDetailView;

	public UiFactoryImpl() {
		super();
	}

	public void showRpcError(Throwable caught, String msg, final HasEnabled hasEnabled) {
		// Create the popup dialog box
		final DialogBox dialogBox = new DialogBox();
		dialogBox.setText("Remote Procedure Call - Fehler");
		dialogBox.setAnimationEnabled(true);
		final Button closeButton = new Button("Close");

		// We can set the id of a widget by accessing its Element
		closeButton.getElement().setId("closeButton");

		final Label textToServerLabel = new Label();
		textToServerLabel.setText(msg);

		final HTML serverResponseLabel = new HTML();
		serverResponseLabel.setText("");
		serverResponseLabel.addStyleName("serverResponseLabelError");
		serverResponseLabel.setHTML(SERVER_ERROR + "<p>Original error message:<br/>" + caught.getLocalizedMessage() + "</p>");

		VerticalPanel dialogVPanel = new VerticalPanel();
		dialogVPanel.addStyleName("dialogVPanel");
		dialogVPanel.add(new HTML("<b>Sent to the server:</b>"));
		dialogVPanel.add(textToServerLabel);
		dialogVPanel.add(new HTML("<br><b>Server replies:</b>"));
		dialogVPanel.add(serverResponseLabel);
		dialogVPanel.setHorizontalAlignment(VerticalPanel.ALIGN_RIGHT);
		dialogVPanel.add(closeButton);
		dialogBox.setWidget(dialogVPanel);

		// Add a handler to close the DialogBox
		closeButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				dialogBox.hide();
				if (hasEnabled != null)
					hasEnabled.setEnabled(true);
			}
		});

		// Show the RPC error message to the user
		dialogBox.center();
		closeButton.setFocus(true);
	}

	@Override public NavigatorView getNavigatorView() {
		if (this.navigatorView == null)
			this.navigatorView = new NavigatorWidget();
		return this.navigatorView;
	}

	@Override public HomeView getHomeView() {
		if (this.homeView == null)
			this.homeView = new HomeWidget();
		return this.homeView;
	}

	@Override public ReleaseQueryView getReleaseQueryView() {
		if (this.releaseQueryView == null)
			this.releaseQueryView = new ReleaseQueryWidget();
		return this.releaseQueryView;
	}

	@Override public MusicQueryView getMusicQueryView() {
		if (this.musicQueryView == null)
			this.musicQueryView = new MusicQueryWidget();
		return this.musicQueryView;
	}

	@Override public SoundtrackQueryView getSoundtrackQueryView() {
		if (this.soundtrackQueryView == null)
			this.soundtrackQueryView = new SoundtrackQueryWidget();
		return this.soundtrackQueryView;
	}

	@Override public SeriesQueryView getSeriesQueryView() {
		if (this.seriesQueryView == null)
			this.seriesQueryView = new SeriesQueryWidget();
		return this.seriesQueryView;
	}

	@Override public ReleaseDetailView getReleaseDetailView() {
		if (this.releaseDetailView == null)
			this.releaseDetailView = new ReleaseDetailWidget();
		return this.releaseDetailView;
	}

	@Override public MusicDetailView getMusicDetailView() {
		if (this.musicDetailView == null)
			this.musicDetailView = new MusicDetailWidget();
		return this.musicDetailView;
	}

	@Override public SoundtrackDetailView getSoundtrackDetailView() {
		if (this.soundtrackDetailView == null)
			this.soundtrackDetailView = new SoundtrackDetailWidget();
		return this.soundtrackDetailView;
	}

	@Override public SeriesDetailView getSeriesDetailView() {
		if (this.seriesDetailView == null)
			this.seriesDetailView = new SeriesDetailWidget();
		return this.seriesDetailView;
	}

	public static Column<Entity, SafeHtml> createCompactReleaseColumn() {
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

				SafeHtmlBuilder sb = new SafeHtmlBuilder();
				sb.appendHtmlConstant("<b>" + release.getTitleInfo() + "</b>");
				sb.appendHtmlConstant("<br>");
				sb.appendHtmlConstant(release.getCatalogInfo());
				sb.appendHtmlConstant("<br>");
				sb.appendHtmlConstant("<i>" + release.getType() + " von " + release.getYearInfo() + "</i>");
				return sb.toSafeHtml();
			}
		};
		return releaseColumn;
	}

	public static Column<Entity, SafeHtml> createCompactMusicColumn() {
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

				String authors = music.getAuthors();
				String artist = music.getArtist();
				if (artist != null)
					artist += " (" + music.getYearInfo() + ")";

				SafeHtmlBuilder sb = new SafeHtmlBuilder();
				sb.appendHtmlConstant((artist == null) ? "&nbsp;" : artist);
				sb.appendHtmlConstant("<br>");
				sb.appendHtmlConstant("<b>" + music.getTitleInfo() + "</b>");
				sb.appendHtmlConstant("<br>");
				sb.appendHtmlConstant((authors == null) ? "&nbsp;" : ("<i>(" + authors + ")</i>"));
				return sb.toSafeHtml();
			}
		};
		return musicColumn;
	}

	public static Column<Entity, SafeHtml> createCompactSeriesColumn() {
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

	public static void addSoundtrackColumns(DataGrid<Entity> table, boolean compact, boolean push) {
		TextColumn<Entity> soundtrackColumn = new TextColumn<Entity>() {
			@Override public String getValue(Entity entity) {
				return "#" + ((Soundtrack) entity).getId();
			}
		};
		// Make the columns sortable.
		soundtrackColumn.setSortable(true);

		// Add the column.
		table.addColumn(soundtrackColumn, "ID");
		table.setColumnWidth(soundtrackColumn, 30.0, Unit.PX);

		// Add the release column(s)
		addReleaseColumns(table, compact, false);

		// Add the music column(s)
		addMusicColumns(table, compact, false);

		if (push)
			table.getColumnSortList().push(soundtrackColumn);
	}

	public static void addReleaseColumns(DataGrid<Entity> table, boolean compact, boolean push) {
		if (compact) {
			Column<Entity, SafeHtml> releaseColumn = createCompactReleaseColumn();
			// Make the columns sortable.
			releaseColumn.setSortable(true);

			table.addColumn(releaseColumn, "Veröffentlichung");
			table.setColumnWidth(releaseColumn, 300.0, Unit.PX);

			if (push)
				table.getColumnSortList().push(releaseColumn);
			return;
		}

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

		if (push)
			table.getColumnSortList().push(titleColumn);

	}

	public static void addMusicColumns(DataGrid<Entity> table, boolean compact, boolean push) {
		if (compact) {
			Column<Entity, SafeHtml> musicColumn = createCompactMusicColumn();
			// Make the columns sortable.
			musicColumn.setSortable(true);

			table.addColumn(musicColumn, "Musik");
			table.setColumnWidth(musicColumn, 300.0, Unit.PX);

			if (push)
				table.getColumnSortList().push(musicColumn);
			return;
		}

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

		if (push)
			table.getColumnSortList().push(titleColumn);
	}

	public static void addReleaseMusicColumns(DataGrid<Entity> table, boolean compact, boolean push) {
		TextColumn<Entity> soundtrackColumn = new TextColumn<Entity>() {
			@Override public String getValue(Entity entity) {
				return "#" + ((Soundtrack) entity).getId();
			}
		};
		// Make the columns sortable.
		soundtrackColumn.setSortable(true);

		// Add the column.
		table.addColumn(soundtrackColumn, "ID");
		table.setColumnWidth(soundtrackColumn, 30.0, Unit.PX);

		// Add the music column(s)
		addMusicColumns(table, compact, false);

		if (push)
			table.getColumnSortList().push(soundtrackColumn);
	}

	public static void addSeriesColumns(DataGrid<Entity> table, boolean compact, boolean push) {
		if (compact) {
			Column<Entity, SafeHtml> seriesColumn = createCompactSeriesColumn();
			// Make the columns sortable.
			seriesColumn.setSortable(true);

			table.addColumn(seriesColumn, "Serien");
			table.setColumnWidth(seriesColumn, 300.0, Unit.PX);

			if (push)
				table.getColumnSortList().push(seriesColumn);
			return;
		}

		TextColumn<Entity> titleColumn = new TextColumn<Entity>() {
			@Override public String getValue(Entity entity) {
				return ((Series) entity).getTitle();
			}
		};

		// Make the columns sortable.
		titleColumn.setSortable(true);

		table.addColumn(titleColumn, "Titel");
		table.setColumnWidth(titleColumn, 200.0, Unit.PX);

		if (push)
			table.getColumnSortList().push(titleColumn);
	}
}

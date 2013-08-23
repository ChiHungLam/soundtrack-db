package dev.sdb.client.view.desktop.detail;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.client.ui.Widget;

import dev.sdb.client.view.MusicDetailView;
import dev.sdb.client.view.desktop.detail.master.MasterDataWidget;
import dev.sdb.client.view.desktop.detail.master.MusicMasterData;
import dev.sdb.client.view.desktop.detail.sublist.SublistWidget;
import dev.sdb.shared.model.entity.Entity;
import dev.sdb.shared.model.entity.Music;

public class MusicDetailWidget extends DetailWidget implements MusicDetailView {

	interface MusicDetailWidgetUiBinder extends UiBinder<Widget, MusicDetailWidget> {}
	private static MusicDetailWidgetUiBinder uiBinder = GWT.create(MusicDetailWidgetUiBinder.class);

	@UiField MusicMasterData musicMasterData;
	@UiField SublistWidget musicReleaseList;

	public MusicDetailWidget() {
		super();
		initWidget(uiBinder.createAndBindUi(this));

		initSublist();
	}

	@Override public DataGrid<Entity> getSublistTable() {
		return this.musicReleaseList.getTable();
	}

	@Override protected MasterDataWidget getMasterDataWidget() {
		return this.musicMasterData;
	}

	@Override protected SublistWidget getSublist() {
		return this.musicReleaseList;
	}

	public void initEntity(Entity entity) {
		if (entity == null) {
			this.musicMasterData.initEntity(null);
			this.musicReleaseList.setElementVisibility(-1);
		} else {
			Music music = (Music) entity;
			this.musicMasterData.initEntity(music);
			getPresenter().getMusicReleaseListFromServer(this);
		}
	}

	@Override public void setPresenter(MusicDetailView.Presenter presenter) {
		super.setPresenter(presenter);
	}

	@Override protected MusicDetailView.Presenter getPresenter() {
		return (MusicDetailView.Presenter) super.getPresenter();
	}

}

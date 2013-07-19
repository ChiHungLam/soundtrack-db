package dev.sdb.client.view.desktop;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;

import dev.sdb.client.presenter.ContentPresenterType;
import dev.sdb.client.view.SectionInfoView;

public class CubicSectionInfoWidget extends Composite implements SectionInfoView {

	interface CubicSectionInfoWidgetUiBinder extends UiBinder<Widget, CubicSectionInfoWidget> {}
	private static CubicSectionInfoWidgetUiBinder uiBinder = GWT.create(CubicSectionInfoWidgetUiBinder.class);

	@UiField FlowPanel cube;

	@SuppressWarnings("unused") private Presenter presenter;

	public CubicSectionInfoWidget() {
		super();
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	@Override public void display(ContentPresenterType type) {
		int face;

		switch (type) {
		case HOME: //Face 1
			face = 1;
			break;
		case RELEASE: //Face 2
			face = 2;
			break;
		case MUSIC: //Face 3
			face = 3;
			break;
		case SOUNDTRACK: //Face 4
			face = 4;
			break;
		case SERIES: //Face 5
			face = 5;
			break;
		default: //Face 6
			face = 6;
			break;
		}
		String transformation = getCubeTransformation(face);

		transformation = UiFactoryImpl.getBrowserPrefixedCssDefinition("transform", transformation);

		this.cube.getElement().setAttribute("style", transformation);
	}

	private String getCubeTransformation(int face) throws IllegalArgumentException {
		int xAngle;
		int yAngle;

		switch (face) {
		case 1://Face 1
			xAngle = 0;
			yAngle = 0;
			break;

		case 2://Face 2
			xAngle = 90;
			yAngle = 0;
			break;

		case 3://Face 3
			xAngle = 0;
			yAngle = -90;
			break;

		case 4://Face 4
			xAngle = 0;
			yAngle = 90;
			break;

		case 5://Face 5
			xAngle = -90;
			yAngle = 0;
			break;

		case 6: // Face 6 (upside down)
			xAngle = 0;
			yAngle = -180;
			break;

		default:
			throw new IllegalArgumentException("illegal cube face index (must be between 1 and 6 incl.): " + face);
		}

		return "rotateX(" + xAngle + "deg) rotateY(" + yAngle + "deg)";
	}

}

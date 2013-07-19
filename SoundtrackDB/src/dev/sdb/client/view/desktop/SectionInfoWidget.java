package dev.sdb.client.view.desktop;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

import dev.sdb.client.presenter.ContentPresenterType;
import dev.sdb.client.view.SectionInfoView;

public class SectionInfoWidget extends Composite implements SectionInfoView {

	interface SectionInfoWidgetUiBinder extends UiBinder<Widget, SectionInfoWidget> {}
	private static SectionInfoWidgetUiBinder uiBinder = GWT.create(SectionInfoWidgetUiBinder.class);

	public interface Style extends CssResource {
		String invisible();
	}

	@UiField protected Style style;

	@UiField FlowPanel cube;

	@UiField Label label1;
	@UiField Label label2;
	@UiField Label label5;
	@UiField Label label6;

	@SuppressWarnings("unused") private Presenter presenter;

	private ContentPresenterType lastType;
	private int lastFace;
	private int factor;

	public SectionInfoWidget() {
		super();
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	@Override public void display(ContentPresenterType type) {
		if (this.lastType == type)
			return;

		if (type == ContentPresenterType.HOME) {
			this.cube.addStyleName(this.style.invisible());
			this.lastType = type;
			return;
		}

		if (this.lastType == ContentPresenterType.HOME) {
			if (this.lastFace == 0)
				this.lastFace = 1;
			this.cube.removeStyleName(this.style.invisible());
			this.lastType = type;
			HasText target = getFaceLabel(this.lastFace);
			target.setText(type.getSectionTitle());
			return;
		}

		//set false for "normal" rolling, set true for inverted rolling
		boolean invertedRolling = true;

		HasText target;

		if (this.lastType == null) {
			target = this.label1;
			this.factor = 0;
			this.lastFace = 1;
		} else {
			if (type.compareTo(this.lastType) < 0) {
				this.lastFace = getNextFace(this.lastFace, invertedRolling);
				this.factor--;
			} else {
				this.lastFace = getNextFace(this.lastFace, !invertedRolling);
				this.factor++;
			}
			target = getFaceLabel(this.lastFace);
		}
		this.lastType = type;



		int xAngle = (this.factor * 90);
		if (invertedRolling)
			xAngle = xAngle * -1;

		String transformation = "rotateX(" + xAngle + "deg) rotateY(0deg)";
		transformation = UiFactoryImpl.getBrowserPrefixedCssDefinition("transform", transformation);

		target.setText(type.getSectionTitle());
		this.cube.getElement().setAttribute("style", transformation);

	}

	private int getNextFace(int current, boolean increment) throws IllegalArgumentException {
		switch (current) {
		case 1:

			return increment ? 2 : 5;
		case 2:

			return increment ? 6 : 1;
		case 6:

			return increment ? 5 : 2;
		case 5:

			return increment ? 1 : 6;
		default:
			throw new IllegalArgumentException("invalid cubic face (allowed values 1, 2, 5, 6): " + current);
		}
	}

	private HasText getFaceLabel(int face) throws IllegalArgumentException {
		switch (face) {
		case 1:
			return this.label1;
		case 2:
			return this.label2;
		case 5:
			return this.label5;
		case 6:
			return this.label6;
		default:
			throw new IllegalArgumentException("invalid cubic face (allowed values 1, 2, 5, 6): " + face);
		}
	}
}

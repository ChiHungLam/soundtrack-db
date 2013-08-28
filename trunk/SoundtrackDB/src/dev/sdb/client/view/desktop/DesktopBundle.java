package dev.sdb.client.view.desktop;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

public interface DesktopBundle extends ClientBundle {

	@Source("img/goto.png") ImageResource gotoIcon();

	@Source("img/catalog_children_entries.png") ImageResource catalogChildrenAndEntriesIcon();

	@Source("img/catalog_children.png") ImageResource catalogChildrenIcon();

	@Source("img/catalog_entries.png") ImageResource catalogEntriesIcon();

	@Source("img/catalog_none.png") ImageResource catalogNoneIcon();

}

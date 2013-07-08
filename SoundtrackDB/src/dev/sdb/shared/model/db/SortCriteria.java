package dev.sdb.shared.model.db;

import com.google.gwt.user.client.rpc.IsSerializable;

public enum SortCriteria implements IsSerializable {
	RELEASE_TYPE,
	RELEASE_CATALOG,
	RELEASE_DATE,
	RELEASE_TITLE,

	MUSIC_GENRE,
	MUSIC_YEAR,
	MUSIC_ARTIST,
	MUSIC_TITLE;

	SortCriteria() {}
}

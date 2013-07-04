package dev.sdb.shared.model;

import com.google.gwt.user.client.rpc.IsSerializable;

public enum SearchScope implements IsSerializable {
	RELEASES_ONLY,
	MUSIC_ONLY,
	SOUNDTRACK;

	SearchScope() {}
}

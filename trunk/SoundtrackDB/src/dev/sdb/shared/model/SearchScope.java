package dev.sdb.shared.model;

import com.google.gwt.user.client.rpc.IsSerializable;

public enum SearchScope implements IsSerializable {
	ALL,
	RELEASES_ONLY,
	MUSIC_ONLY;

	SearchScope() {}
}

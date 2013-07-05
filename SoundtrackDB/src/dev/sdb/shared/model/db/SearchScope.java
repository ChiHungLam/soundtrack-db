package dev.sdb.shared.model.db;

import com.google.gwt.user.client.rpc.IsSerializable;

public enum SearchScope implements IsSerializable {
	RELEASES,
	MUSIC,
	SOUNDTRACK;

	SearchScope() {}
}

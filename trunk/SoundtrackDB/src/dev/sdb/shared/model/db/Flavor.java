package dev.sdb.shared.model.db;

import com.google.gwt.user.client.rpc.IsSerializable;

public enum Flavor implements IsSerializable {
	RELEASES,
	MUSIC,
	SOUNDTRACK;

	Flavor() {}
}

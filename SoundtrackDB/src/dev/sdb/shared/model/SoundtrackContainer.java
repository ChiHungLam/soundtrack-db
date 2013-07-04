package dev.sdb.shared.model;

import com.google.gwt.user.client.rpc.IsSerializable;

public interface SoundtrackContainer extends IsSerializable {

	String getTitle();
	SoundtrackContainerType getType();
}
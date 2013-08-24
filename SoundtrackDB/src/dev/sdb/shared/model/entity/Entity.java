package dev.sdb.shared.model.entity;

import com.google.gwt.user.client.rpc.IsSerializable;

import dev.sdb.shared.model.db.Flavor;

public interface Entity extends IsSerializable {

	/**
	 * @return the id
	 */
	long getId();

	/**
	 * @return the match, a single line unique descriptive String
	 */
	String getMatch();

	Flavor getFlavor();
}

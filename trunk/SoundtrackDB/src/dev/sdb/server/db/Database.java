package dev.sdb.server.db;

import java.io.IOException;

import com.google.gwt.view.client.Range;

import dev.sdb.shared.model.db.Flavor;
import dev.sdb.shared.model.db.Result;
import dev.sdb.shared.model.entity.Entity;

public interface Database {

	void open() throws IOException;
	void close();

	Entity getEntity(Flavor flavor, long id) throws IOException;

	Result getReleaseSoundtrackList(long audioId) throws IOException;

	Result getMusicReleaseList(long versionId, Range range) throws IOException;

	Result getSeriesReleaseList(long seriesId, Range range) throws IOException;

	Result getCatalogReleaseList(long catalogId, Range range) throws IOException;

	Result query(Flavor flavor, String term, Range range, boolean ascending) throws IOException;

	Result getCatalogList(long parentCatalogId) throws IOException;

	void repairCatalogInfo() throws IOException;


}

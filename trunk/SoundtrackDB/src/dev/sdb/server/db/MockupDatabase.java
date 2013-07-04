package dev.sdb.server.db;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Vector;

import com.google.gwt.view.client.Range;

import dev.sdb.shared.model.Music;
import dev.sdb.shared.model.Release;
import dev.sdb.shared.model.SearchResult;
import dev.sdb.shared.model.SearchResultSort;
import dev.sdb.shared.model.SearchScope;
import dev.sdb.shared.model.SoundtrackContainer;

public class MockupDatabase {

	private static final List<Release> ALL_RELEASES = Arrays.asList(new Release("Die drei ??? (6) und der sprechende Totenkopf"), new Release("Flash Gordon (7) Das grüne Ungeheuer"), new Release("Hui Buh (23) und das furchtbare Phantom"), new Release("TKKG (2) Der blinde Hellseher"), new Release("Larry Brent (1) Irrfahrt der Skelette"), new Release("Perry Rhodan - Planet des Todes"));
	private static final List<Music> ALL_MUSIC = Arrays.asList(new Music("Perry Rhodan (Teil 1)"), new Music("Flaggio Let Me Be Like I Am (Part 3)"), new Music("Aufbruch und Verfolgungsjagd"), new Music("\"Radio-Musik, Nr. 1\""), new Music("Dusty Banjo"));

	public SearchResult querySoundtrackContainer(String term, SearchScope scope, Range range, final SearchResultSort sort) {
		int start = range.getStart();
		int length = range.getLength();
		int end = start + length;

		term = term.toLowerCase();

		List<SoundtrackContainer> totalResult = new Vector<SoundtrackContainer>();

		switch (scope) {
		case RELEASES:
			for (Release release : ALL_RELEASES) {
				if (release.getTitle().toLowerCase().contains(term))
					totalResult.add(release);
			}
			break;
		case MUSIC:
			for (Music music : ALL_MUSIC) {
				if (music.getTitle().toLowerCase().contains(term))
					totalResult.add(music);
			}
			break;
		case SOUNDTRACK:

			break;

		default:
			break;
		}

		final Comparator<SoundtrackContainer> titleComparator = new Comparator<SoundtrackContainer>() {
			public int compare(SoundtrackContainer o1, SoundtrackContainer o2) {
				if (o1 == o2) {
					return 0;
				}

				// Compare the name columns.
				int diff = -1;
				if (o1 != null) {
					diff = (o2 != null) ? o1.getTitle().compareTo(o2.getTitle()) : 1;
				}
				return sort.isAscending() ? diff : -diff;
			}
		};

		Comparator<SoundtrackContainer> comparator;
		switch (sort.getType()) {
		case TITLE:
			comparator = titleComparator;
			break;
		case TYPE:
			final Comparator<SoundtrackContainer> typeComparator = new Comparator<SoundtrackContainer>() {
				public int compare(SoundtrackContainer o1, SoundtrackContainer o2) {
					int result;

					if (o1 == o2) {
						result = 0;
					} else {
						int diff = -1;
						if (o1 != null) {
							diff = (o2 != null) ? o1.getType().compareTo(o2.getType()) : 1;
						}
						result = sort.isAscending() ? diff : -diff;
					}

					if (result != 0)
						return result;
					return titleComparator.compare(o1, o2);
				}
			};
			comparator = typeComparator;
			break;

		default:
			comparator = titleComparator;
			break;
		}

		Collections.sort(totalResult, comparator);

		List<SoundtrackContainer> chunk;
		if (length > totalResult.size())
			chunk = totalResult;
		else
			chunk = totalResult.subList(start, end);

		String info = "Letzte Suche: nach '" + term + "' im Bereich '" + scope.name() + "'.";

		return new SearchResult(info, new Vector<SoundtrackContainer>(chunk), totalResult.size());
	}

}

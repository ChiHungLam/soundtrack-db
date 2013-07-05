package dev.sdb.shared;

/**
 * <p>
 * SearchTermVerifier validates that the search term the user enters is valid.
 * </p>
 */
public class SearchTermVerifier {

	public static final int SEARCH_TERM_MIN_LENGTH = 1;

	/**
	 * Verifies that the specified search term is valid for our service.
	 * 
	 * @param term
	 *            the search term to validate
	 * @return <code>true</code> if valid, otherwise <code>false</code>
	 */
	public static boolean isValidSearchTerm(String term) {
		if (term == null) {
			return false;
		}
		return term.length() >= SEARCH_TERM_MIN_LENGTH;
	}
}

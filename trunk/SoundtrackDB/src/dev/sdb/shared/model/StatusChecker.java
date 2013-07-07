package dev.sdb.shared.model;

public class StatusChecker {

	/**
	 * Tests if <code>bit</code> is set in <code>byteValue</code>. Byte ordering is from right to left.
	 * 
	 * @param byteValue
	 *            The byte to read from.
	 * @param bit
	 *            The bit to test. A value from 0 to 31.
	 * @return Returns <code>true</code> if the bit is set, otherwise <code>false</code>.
	 * @throws IllegalArgumentException
	 *             If bit is out of range, i.e. not a value from 0 to 31.
	 */
	public static boolean isBitSet(int byteValue, int bit) throws IllegalArgumentException {
		if (bit < 0 || bit > 31)
			throw new IllegalArgumentException("bit must not be less than 0 or greater than 31.");
		return ((byteValue & (1L << bit)) != 0);
	}

}

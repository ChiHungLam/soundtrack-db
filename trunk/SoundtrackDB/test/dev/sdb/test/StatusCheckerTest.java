package dev.sdb.test;


import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import dev.sdb.shared.model.StatusChecker;

public class StatusCheckerTest {



	private void test(int byteValue, int bit, boolean condition) {
		String msg = "bit " + bit + " in " + byteValue + " [0x " + Integer.toHexString(byteValue) + "] [0b " + Integer.toBinaryString(byteValue) + "]";
		//		System.out.println("Testing " + msg);

		if (condition)
			assertTrue(msg + "should have been " + condition, StatusChecker.isBitSet(byteValue, bit));
		else
			assertFalse(msg + "should have been " + condition, StatusChecker.isBitSet(byteValue, bit));
	}

	@Test public void isBitSet() {

		int zero = 0;
		int one = 1;
		int maxInt = Integer.MAX_VALUE; // with bit 31 not set
		int minInt = Integer.MIN_VALUE; // with only bit 31 set
		int allBits = -1; // all bits are set
		int arbitrary = 123123;

		for (int i = 0; i < 32; i++) {
			test(zero, i, false);
		}


		test(one, 0, true);
		for (int i = 1; i < 32; i++) {
			test(one, i, false);
		}


		for (int i = 0; i < 31; i++) {
			test(maxInt, i, true);
		}
		test(maxInt, 31, false);


		for (int i = 0; i < 31; i++) {
			test(minInt, i, false);
		}
		test(minInt, 31, true);

		for (int i = 0; i < 32; i++) {
			test(allBits, i, true);
		}

		try {
			test(arbitrary, -1, false);
			assertFalse("No IllegalArgumentException thrown", true);
		} catch (IllegalArgumentException e) {
			// this should always be thrown
		}

		try {
			test(arbitrary, 32, false);
			assertFalse("No IllegalArgumentException thrown", true);
		} catch (IllegalArgumentException e) {
			// this should always be thrown
		}
	}

}

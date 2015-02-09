package tests;

import rhit.jrProj.henry.helpers.Checkers;
import junit.framework.TestCase;

public class CheckersTest extends TestCase {
	
	public void testNotNegative_Positive() {
		try {
			Checkers.checkNotNegative(1);
		} catch (Exception e) {
			fail();
		}
	}
	
	public void testNotNegative_0() {
		try {
			Checkers.checkNotNegative(0);
		} catch (Exception e) {
			fail();
		}
	}
	
	public void testNotNegative_Negative() {
		try {
			Checkers.checkNotNegative(-1);
		} catch (Exception e) {
			return;
		}
		fail();
	}
	
	public void testNotNull_Null() {
		try {
			Checkers.checkNotNull(null);
		} catch (Exception e) {
			return;
		}
		fail();
	}
	
	public void testNotNull_NotNull() {
		Integer integer = new Integer(3);
		try {
			Checkers.checkNotNull(integer);
		} catch (Exception e) {
			fail();
		}
		return;
	}

}

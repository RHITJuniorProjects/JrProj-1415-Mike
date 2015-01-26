package tests;

import junit.framework.TestCase;

public class HorizontalPickerTest extends TestCase {

	private int minValue;
	private int maxValue;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		minValue = 0;
		maxValue = 100;
	}

	public void testUnderMin() {

		int value = 5;
		for (int i = 0; i <= 5; i++) {
			if (value - 1 < minValue) {
				value = maxValue;
			} else {
				value--;
			}

		}
		assertEquals(100, value);
	}

	public void testOverMax() {

		int minValue = 0;
		int maxValue = 100;
		int value = 95;
		for (int i = 0; i <= 5; i++) {
			if (value + 1 > maxValue) {
				value = minValue;
			} else {
				value++;
			}

		}
		assertEquals(0, value);
	}
}

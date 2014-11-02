import junit.framework.TestCase;
import rhit.jrProj.henry.firebase.Map;

public class MapTest extends TestCase {

	private Map<String, String> map;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.map = new Map<String, String>();
		this.map.put("Five", "5");
	}

	public void testGetValue() {

		assertEquals("5", this.map.getValue("Five"));
		try {
			this.map.getValue("Ten");
			assertTrue(false);
		} catch (Exception e) {
			assertTrue(true);
		}
	}

	public void testReplaceValue() {
		assertTrue(this.map.replaceValue("Five", "5+9"));
		assertFalse(this.map.getAllValues().contains("5"));
		assertTrue(this.map.getAllValues().contains("5+9"));
	}

	public void testPut() {
		assertTrue(this.map.put("Four", "4"));
		assertFalse(this.map.put("Five", "5+9"));
		assertTrue(this.map.getAllKeys().contains("Four"));
		assertFalse(this.map.getAllValues().contains("5+9"));
		assertEquals(2, this.map.size());
	}

	public void testRemove() {
		assertTrue(this.map.remove("Five"));
		assertEquals(0, this.map.size());
		assertFalse(this.map.remove("Ten"));
	}

	public void testSize() {
		assertEquals(1, this.map.size());
	}

}

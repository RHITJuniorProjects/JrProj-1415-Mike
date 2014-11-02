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
		assertTrue(this.map.put("Four", "4"));
		assertFalse(this.map.put("Five", "5+9"));
		assertTrue(this.map.getAllKeys().contains("Four"));
		assertFalse(this.map.getAllValues().contains("5+9"));
	}

	public void testReplaceValue() {
		fail("Not yet implemented");
	}

	public void testPut() {
		fail("Not yet implemented");
	}

	public void testRemove() {
		fail("Not yet implemented");
	}

	public void testSize() {
		fail("Not yet implemented");
	}

}

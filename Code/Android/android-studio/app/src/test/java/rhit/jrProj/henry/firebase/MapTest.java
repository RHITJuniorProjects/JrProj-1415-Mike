package rhit.jrProj.henry.firebase;

import junit.framework.TestCase;

import java.util.ArrayList;

public class MapTest extends TestCase {

    public Map<String, String> map = new Map<String, String>();


    @Override
    protected void setUp() throws Exception {
        super.setUp();
        this.map.put("test1", "test");
        this.map.put("test2", "test2");
    }

    public void testGetValue() throws Exception {

        String returned = this.map.getValue("test1");
        assertEquals("test", returned);
        assertEquals("test2", this.map.getValue("test2"));

    }

    public void testReplaceValue() throws Exception {
        this.map.replaceValue("test1", "testing");
        assertEquals("testing", this.map.getValue("test1"));

        this.map.replaceValue("test1", "testy");
        assertEquals("testy", this.map.getValue("test1"));

        this.map.replaceValue("test2", "testing");

        assertEquals("testing", this.map.getValue("test2"));
    }

    public void testGetAllValues() throws Exception {
        ArrayList<String> allValues;
        allValues = this.map.getAllValues();
        assertEquals(2, allValues.size());

        assertEquals(true, allValues.contains("test"));
        assertEquals(true, allValues.contains("test2"));

    }

    public void testGetAllKeys() throws Exception {
        ArrayList<String> allKeys;
        allKeys = this.map.getAllKeys();
        assertEquals(2, allKeys.size());

        assertEquals(true, allKeys.contains("test1"));
        assertEquals(true, allKeys.contains("test2"));

    }

    public void testPut() throws Exception {
        this.map.put("testPut", "putTested");

        assertEquals(true, this.map.containsKey("testPut"));
    }

    public void testRemove() throws Exception {
        this.map.remove("test1");
        assertEquals(false, this.map.containsKey("test1"));

        assertEquals(1, this.map.size());
    }

    public void testSize() throws Exception {
        assertEquals(2, this.map.size());

        this.map.remove("test1");

        this.map.remove("test2");

        assertEquals(0, this.map.size());
    }

    public void testVerify() throws Exception {

        assertEquals(true, this.map.verify());

    }

    public void testContainsKey() throws Exception {
        assertTrue(this.map.containsKey("test1"));
        assertTrue(this.map.containsKey("test2"));
        assertFalse(this.map.containsKey("notPresent"));
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

}
package rhit.jrProj.henry.firebase;

import junit.framework.TestCase;

public class DueDateTest extends TestCase {
    public DueDate dueDate;

    public void setUp() throws Exception {
        super.setUp();
        this.dueDate = new DueDate(22, 10, 1993);
    }


    public void testToString() throws Exception {
        assertEquals("1993-10-22", this.dueDate.toString());
    }

    public void tearDown() throws Exception {

    }
}
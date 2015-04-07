import junit.framework.TestCase;


/**
 * Created by hullzr on 4/6/2015.
 */
public class simpleUnitTest extends TestCase{

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    public void testSimpleTest(){
        assertEquals(5,5);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

}

import junit.framework.TestCase;

import rhit.jrProj.henry.CreateMilestoneFragment;

import org.mockito.Mockito;

import static org.mockito.Mockito.*;

/**
 * Created by hullzr on 4/30/2015.
 */
public class CreateMilestoneFragmentTest extends TestCase {

    public CreateMilestoneFragment fragment = new CreateMilestoneFragment();

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    public void testCreateMilestoneFragment(){
        CreateMilestoneFragment frag = new CreateMilestoneFragment();

        assertEquals(true, frag instanceof CreateMilestoneFragment);
    }

    public void testCreateMilestone(){
        CreateMilestoneFragment frag = new CreateMilestoneFragment();
        frag.createMilestoneForTesting();
        String name = (String)frag.fakeFirebase.map.get("name");
        assertEquals("testName", name);

        String des = (String) frag.fakeFirebase.map.get("description");
        assertEquals("description", des);



    }



    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }
}

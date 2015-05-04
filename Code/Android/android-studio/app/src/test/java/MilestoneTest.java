import rhit.jrProj.henry.firebase.FakeFirebase;
import rhit.jrProj.henry.firebase.Milestone;
import junit.framework.TestCase;

import org.mockito.Mockito;

import static org.mockito.Mockito.*;
import rhit.jrProj.henry.firebase.Milestone;


/**
 * Created by hullzr on 4/28/2015.
 */
public class MilestoneTest extends TestCase{
    public FakeFirebase fakeFirebase = new FakeFirebase();
    public Milestone milestone = new Milestone(fakeFirebase);


    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    public void testCreateMilestone() {

        assertEquals(true, this.milestone instanceof Milestone);
    }

    public void testMilestonetoString(){
        assertEquals("testName", this.milestone.toString());
    }

//    public void testMilestoneEquals(){
//        FakeFirebase fFB = new FakeFirebase();
//       Milestone mockedMilestone = mock(Milestone.class);
//
//
//
//        //get .equals to return equals(this.milestone.getfirebase.toString,
//        // otherMilestone.getFirebase.toString);
//        when(mockedMilestone.equals(this.milestone)).thenReturn(this.milestone.fakeFirebase.toString().equals("blah"));
//
//        assertEquals(true,mockedMilestone.equals(this.milestone));
//
//    }
//
// ///////////////////////////////////////////////
// cannot stub a .equals() method with Mockito////
//////////////////////////////////////////////////




    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

}

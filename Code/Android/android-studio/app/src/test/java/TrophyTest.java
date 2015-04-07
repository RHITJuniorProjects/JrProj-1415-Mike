import android.content.Context;
import android.os.Parcel;

import com.firebase.client.Firebase;

import junit.framework.TestCase;

import static org.mockito.Mockito.*;

import rhit.jrProj.henry.firebase.Trophy;

/**
 * Created by hullzr on 4/6/2015.
 */
public class TrophyTest extends TestCase{

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }
    

    public void testCreateTrophy(){
        Parcel mockedParcel = mock(Parcel.class);

        Context mockedContext = mock(Context.class);

        when(mockedParcel.readString()).thenReturn("https://henry-test.firebaseio.com/");
        Trophy trophy = new Trophy(mockedParcel);
        trophy.getFirebase().setAndroidContext(mockedContext);

        assertEquals(true, trophy instanceof Trophy);


    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }
}

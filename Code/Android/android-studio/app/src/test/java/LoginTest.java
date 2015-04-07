import com.firebase.client.Firebase;


import junit.framework.TestCase;

import rhit.jrProj.henry.LoginActivity;
import rhit.jrProj.henry.bridge.Login;
import static org.mockito.Mockito.*;


/**
 * Created by hullzr on 4/6/2015.
 */
public class LoginTest extends TestCase{

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }


    public void testCreateLogin(){
        Firebase mockedFB = mock(Firebase.class);
        LoginActivity mockedLA = mock(LoginActivity.class);

        Login log = new Login(mockedFB, mockedLA);

        assertEquals(true, log instanceof Login);
    }

    public void testLoginMethods(){
        Firebase mockedFB = mock(Firebase.class);
        LoginActivity mockedLA = mock(LoginActivity.class);

        Login log = new Login(mockedFB, mockedLA);

        String email = "hullzr@rose-hulman.edu";
        String password = "password";

        assertEquals(true, log.isPasswordValid(email));

        assertEquals(true, log.isPasswordValid(password));


    }




    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }
}

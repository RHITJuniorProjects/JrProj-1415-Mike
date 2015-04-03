package rhit.jrProj.henry.bridge;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import rhit.jrProj.henry.LeaderboardActivity;
import rhit.jrProj.henry.LoginActivity;

/**
 * Created by willisaj on 4/3/2015.
 */
public class Login  {

    private Firebase mFirebaseRef;

    private LoginActivity mActivity;

    /* Data from the authenticated user */
    private AuthData authData;

    public Login(Firebase firebaseRef, LoginActivity loginActivity) {
        this.mFirebaseRef = firebaseRef;
        this.mActivity = loginActivity;
    }

    /**
     * Authenticates the user with Firebase, using the specified email address and password
     *
     * @param username
     * @param password
     */
    public void loginWithPassword(String username, String password) {
        this.mFirebaseRef.authWithPassword(username, password, new AuthResultHandler(
                "password"));
    }

    /**
     * Authenticates a user to allow them to login.
     *
     * @param authData
     */
    private void setAuthenticatedUser(AuthData authData) {
        if (authData != null) {
            this.authData = authData;
            this.mActivity.openProjectListView(this.authData);
        }
    }

    /**
     * Verifies the user provided a valid email.
     *
     * @param email
     * @return
     */
    public boolean isEmailValid(String email) {
        return email.contains("@");
    }

    /**
     * Verifies the password is longer than 4 characters.
     *
     * @param password
     * @return
     */
    public boolean isPasswordValid(String password) {
        return password.length() > 4;
    }

    /**
     * A custom AuthResultHandler
     *
     * @author daveyle
     */
    private class AuthResultHandler implements Firebase.AuthResultHandler {

        private final String provider;

        /**
         * Creates a new AuthResultHandler
         *
         * @param provider
         */
        public AuthResultHandler(String provider) {
            this.provider = provider;
        }

        /**
         * On successful authentication, calls the setAuthenticatedUser method
         */
        public void onAuthenticated(AuthData authData) {
            setAuthenticatedUser(authData);
        }

        /**
         * On failed login, displays an error message
         */
        public void onAuthenticationError(FirebaseError firebaseError) {
            mActivity.showError(firebaseError.toString());
        }

    }
}

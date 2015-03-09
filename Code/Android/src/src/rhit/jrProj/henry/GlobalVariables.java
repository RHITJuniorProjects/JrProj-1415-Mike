package rhit.jrProj.henry;

import rhit.jrProj.henry.firebase.User;
import android.app.Application;

public class GlobalVariables extends Application {

	/**
	 * If the application is in tablet mode or not.
	 */
	private boolean mTwoPane;
	/**
	 * Created user after login
	 */
	private static User user;

	/**
	 * The Url to the firebase repository
	 */
	private static final String firebaseUrl = "https://henry-test.firebaseio.com/";
	
	
	public boolean ismTwoPane() {
		return mTwoPane;
	}

	public void setmTwoPane(boolean mTwoPane) {
		this.mTwoPane = mTwoPane;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public static String getFirebaseUrl() {
		return firebaseUrl;
	}

}

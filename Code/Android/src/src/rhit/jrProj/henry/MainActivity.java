package rhit.jrProj.henry;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;

public class MainActivity extends Activity {
	public final static String firebaseLoc = "https://shining-inferno-2277.firebaseio.com/";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Firebase.setAndroidContext(this);
		setContentView(R.layout.activity_main);

		Firebase ref = new Firebase(firebaseLoc);

		AuthData authData = null;
		if (authData != null) {
			Intent intent = new Intent(this, ProjectListActivity.class);
			intent.putExtra("user", firebaseLoc + "users/" + authData.getUid());
			this.startActivity(intent);
		} else {
			Intent intent = new Intent(this, LoginActivity.class);
			this.startActivity(intent);
		}
		this.finish();
	}

	/**
	 * The method that is called when the "Login" button is pressed.
	 * 
	 * @param view
	 */

	public void openLoginDialog(View view) {

		Intent intent = new Intent(this, LoginActivity.class);
		this.startActivity(intent);

	}
}

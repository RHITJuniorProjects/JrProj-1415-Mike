package rhit.jrProj.henry;

import rhit.jrProj.henry.firebase.User;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;

public class WelcomeActivity extends Activity implements OnClickListener {

	private static User mUser;
	private GlobalVariables mGlobalVariables;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mGlobalVariables = ((GlobalVariables) getApplicationContext());
		Firebase.setAndroidContext(this);
		Firebase ref = new Firebase(mGlobalVariables.getFirebaseUrl());
		AuthData authData = ref.getAuth();

		if (authData != null) {
			mUser = new User(mGlobalVariables.getFirebaseUrl() + "users/"
					+ authData.getUid());
		} else if (this.getIntent().getStringExtra("user") != null) {
			// If logged in get the user's project list
			mUser = new User(this.getIntent().getStringExtra("user"));
		} else {
			Intent intent = new Intent(this, LoginActivity.class);
			this.startActivity(intent);
			this.finish();
		}
		setContentView(R.layout.activity_welcome);
		findViewById(R.id.go_to_projects_btn).setOnClickListener(this);
		findViewById(R.id.go_to_metrics_btn).setOnClickListener(this);
		findViewById(R.id.go_to_profile_btn).setOnClickListener(this);
		findViewById(R.id.go_to_tasks_btn).setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.welcome, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId() == R.id.welcome_logout){
			Intent login = new Intent(this, LoginActivity.class);
			this.mUser = null;
			mGlobalVariables.setUser(null);
			this.startActivity(login);
			this.finish();
			Firebase ref = new Firebase(mGlobalVariables.getFirebaseUrl());
			ref.unauth();
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.go_to_profile_btn:
			intent = new Intent(this, ProfileActivity.class);
			startActivity(intent);
			break;
		case R.id.go_to_metrics_btn:

			// intent = new Intent(this, MainActivity.class);
			// startActivity(intent);
			break;
		case R.id.go_to_projects_btn:
			intent = new Intent(this, MainActivity.class);
			intent.putExtra("user", mUser.toString());
			startActivity(intent);
			break;
		case R.id.go_to_tasks_btn:
			// intent = new Intent(this, MainActivity.class);
			// startActivity(intent);
			break;
		default:
			break;
		}
	}
}

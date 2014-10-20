package rhit.jrProj.henry;

import java.util.ArrayList;
import java.util.Map;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;

import rhit.jrProj.henry.firebase.Project;
import rhit.jrProj.henry.firebase.User;
import rhit.jrProj.henry.firebase.Enums.Role;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Firebase.setAndroidContext(this);
		setContentView(R.layout.activity_main);
		
		Firebase ref = new Firebase("https://shining-inferno-2277.firebaseio.com/");
		
		AuthData authData = ref.getAuth();
		if (authData != null) {
			openProjectListView(authData);
		} else {
			 Intent intent = new Intent(this, LoginActivity.class);
			 this.startActivity(intent);
		}
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
	
	// public void openProjectListView(View view)
	// {
	// ArrayList<Project> projects = new ArrayList<Project>();
	//
	// projects.add(new
	// Project("https://henry-staging.firebaseio.com/projects/-JYkWFRJRG5eZ1S85iKL"));
	// projects.add(new
	// Project("https://henry-staging.firebaseio.com/projects/-JYcg488tAYS5rJJT4Kh"));
	//
	// Intent intent = new Intent(this, ProjectListActivity.class);
	// intent.putParcelableArrayListExtra("Projects", projects);
	// this.startActivity(intent);
	// }
	
	/**
	 * Opens up a list list of projects after logging in. 
	 * @param authdata
	 */
	public void openProjectListView(AuthData authdata) {

//		mAuthProgressDialog.hide();
		Intent intent = new Intent(this, ProjectListActivity.class);
		intent.putExtra(
				"user",
				"https://shining-inferno-2277.firebaseio.com/users/"
						+ authdata.getUid());
		this.startActivity(intent);
	}

}

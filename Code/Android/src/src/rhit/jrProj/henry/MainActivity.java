package rhit.jrProj.henry;

import java.util.ArrayList;

import com.firebase.client.Firebase;

import rhit.jrProj.henry.firebase.Project;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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
		Log.i("Test", "Start");
	}
	
	/**
	 * The method that is called when the "Open Milestone" button
	 * is pressed.
	 * @param view
	 */
	public void openLoginDialog(View view)
	{
		Intent intent = new Intent(this, LoginActivity.class);
//		intent.putParcelableArrayListExtra("Projects", projects);
		this.startActivity(intent);
	}
//	public void openProjectListView(View view)
//	{
//		ArrayList<Project> projects = new ArrayList<Project>();
//		
//		projects.add(new Project("https://henry-staging.firebaseio.com/projects/-JYkWFRJRG5eZ1S85iKL"));
//		projects.add(new Project("https://henry-staging.firebaseio.com/projects/-JYcg488tAYS5rJJT4Kh"));
//		
//		Intent intent = new Intent(this, ProjectListActivity.class);
//		intent.putParcelableArrayListExtra("Projects", projects);
//		this.startActivity(intent);
//	}
	
}

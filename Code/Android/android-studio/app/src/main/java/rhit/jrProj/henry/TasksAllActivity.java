package rhit.jrProj.henry;

import java.util.ArrayList;

import com.firebase.client.Firebase;

import rhit.jrProj.henry.firebase.Milestone;
import rhit.jrProj.henry.firebase.Project;
import rhit.jrProj.henry.firebase.Task;
import rhit.jrProj.henry.firebase.User;
import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;

public class TasksAllActivity extends Activity implements TasksAllListFragment.Callbacks {
	private GlobalVariables mGlobalVariables;
	private User user;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.user=new User(this.getIntent().getStringExtra(
				"user"));
//		mGlobalVariables = ((GlobalVariables) getApplicationContext());
//		String fireBaseUrl = mGlobalVariables.getFirebaseUrl();
//		Firebase firebase = new Firebase(fireBaseUrl);
//
//		String userKey = firebase.getAuth().getUid();
//		Log.i(fireBaseUrl+"users/"+userKey, "URL");
//		this.user=new User(fireBaseUrl+"users/"+userKey);
		setContentView(R.layout.activity_tasks_all);
		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new TasksAllListFragment()).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.tasks_all, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onItemSelected(Task t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ArrayList<Task> getTasks() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Project getSelectedProject() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User getUser() {
		return this.user;
//		return this.user;
		
	}

	@Override
	public Milestone getSelectedMilestone() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getSortMode() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isFromMainActivity() {
		// TODO Auto-generated method stub
		return false;
	}

	
}

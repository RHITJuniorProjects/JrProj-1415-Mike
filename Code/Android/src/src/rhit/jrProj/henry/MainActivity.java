package rhit.jrProj.henry;

import java.util.ArrayList;

import rhit.jrProj.henry.firebase.Milestone;
import rhit.jrProj.henry.firebase.Project;
import rhit.jrProj.henry.firebase.Task;
import rhit.jrProj.henry.firebase.User;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;

public class MainActivity extends Activity implements
		ProjectListFragment.Callbacks, MilestoneListFragment.Callbacks,
		TaskListFragment.Callbacks {
	/**
	 * TODO something here
	 */
	public final static String firebaseLoc = "https://henry-test.firebaseio.com/";
	/**
	 * The two pane determining boolean
	 */
	private boolean mTwoPane;
	/**
	 * The project that has been selected from the list
	 */
	private Project selectedProject;
	/**
	 * Created user after login
	 */
	private User user;
	/**
	 * The milestone selected by the user
	 */
	private Milestone selectedMilestone;

	/**
	 * The task that is currently selected by the user
	 */
	private Task selectedTask;

	/**
	 * Determines what page to fill in when the application starts
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Firebase.setAndroidContext(this);

		Firebase ref = new Firebase(firebaseLoc);

		AuthData authData = ref.getAuth();
		if (authData != null) {
			// Replaced with fragment TODO delete this
			// Intent intent = new Intent(this, ProjectListActivity.class);
			// intent.putExtra("user", firebaseLoc + "users/" +
			// authData.getUid());
			// this.startActivity(intent);
			this.user = new User(firebaseLoc + "users/" + authData.getUid());
			createProjectList();
		} else if (this.getIntent().getStringExtra("user") != null) {
			// If logged in get the user's project list
			this.user = new User(this.getIntent().getStringExtra("user"));
			createProjectList();
		} else {
			// Starts the LoginActivity if the user has not been logged in just
			// yet
			Intent intent = new Intent(this, LoginActivity.class);
			this.startActivity(intent);
			this.finish();
		}

	}

	/**
	 * Determines if this activity should operate in two pane mode and creates
	 * the fragment to display a list of projects.
	 */
	private void createProjectList() {
		this.mTwoPane = (getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
		Bundle args = new Bundle();
		args.putBoolean("TwoPane", false);
		ProjectListFragment fragment = new ProjectListFragment();
		fragment.setArguments(args);
		if (!this.mTwoPane) {
			setContentView(R.layout.activity_main);
			getFragmentManager().beginTransaction()
					.add(R.id.main_fragment_container, fragment).commit();
			
		} else {
			setContentView(R.layout.activity_project_twopane);
			getFragmentManager().beginTransaction()
					.add(R.id.twopane_list, fragment).commit();
		}
	}

	/**
	 * TODO
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.project, menu);

		return true;
	}

	/**
	 * TODO
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == android.R.id.home) {
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * 
	 * @param view
	 */
	public void openTaskView(View view) {
		int container = this.mTwoPane ? R.id.twopane_list
				: R.id.main_fragment_container;
		Bundle args = new Bundle();
		args.putBoolean("TwoPane", this.mTwoPane);
		TaskListFragment fragment = new TaskListFragment();
		fragment.setArguments(args);
		getFragmentManager().beginTransaction().replace(container, fragment)
				.commit();
		if (this.mTwoPane) {
			getFragmentManager()
					.beginTransaction()
					.remove(getFragmentManager().findFragmentById(
							R.id.twopane_detail_container)).commit();
		}
	}

	/**
	 * Open the MilestoneList Activity for the selected milestone
	 * 
	 * @param view
	 */
	public void openMilestoneView(View view) {
		int container = this.mTwoPane ? R.id.twopane_list
				: R.id.main_fragment_container;
		Bundle args = new Bundle();
		args.putBoolean("TwoPane", this.mTwoPane);
		MilestoneListFragment fragment = new MilestoneListFragment();
		fragment.setArguments(args);
		getFragmentManager().beginTransaction().replace(container, fragment)
				.commit();
		if (this.mTwoPane) {
			getFragmentManager()
					.beginTransaction()
					.remove(getFragmentManager().findFragmentById(
							R.id.twopane_detail_container)).commit();
		}
	}

	/**
	 * Callback method from {@link ProjectListFragment.Callbacks} indicating
	 * that the item with the given ID was selected.
	 */
	public void onItemSelected(Project p) {
		this.selectedProject = p;
		Bundle arguments = new Bundle();
		arguments.putParcelable("Project", p);
		ProjectDetailFragment fragment = new ProjectDetailFragment();
		fragment.setArguments(arguments);
		getFragmentManager()
				.beginTransaction()
				.replace(
						this.mTwoPane ? R.id.twopane_detail_container
								: R.id.main_fragment_container, fragment)
				.commit();

	}

	/**
	 * Callback method from {@link MilestoneListFragment.Callbacks} indicating
	 * that the item with the given ID was selected.
	 */
	public void onItemSelected(Milestone m) {
		this.selectedMilestone = m;
		Bundle arguments = new Bundle();
		arguments.putParcelable("Milestone", m);
		MilestoneDetailFragment fragment = new MilestoneDetailFragment();
		fragment.setArguments(arguments);
		getFragmentManager()
				.beginTransaction()
				.replace(
						this.mTwoPane ? R.id.twopane_detail_container
								: R.id.main_fragment_container, fragment)
				.commit();

	}

	/**
	 * Callback method from {@link ItemListFragment.Callbacks} indicating that
	 * the item with the given ID was selected.
	 */
	public void onItemSelected(Task t) {
		this.selectedTask = t;
		Bundle arguments = new Bundle();
		arguments.putParcelable("Task", t);
		TaskDetailFragment fragment = new TaskDetailFragment();
		fragment.setArguments(arguments);
		getFragmentManager()
				.beginTransaction()
				.replace(
						this.mTwoPane ? R.id.twopane_detail_container
								: R.id.main_fragment_container, fragment)
				.commit();
	}

	/**
	 * Logout the user.
	 */
	public void logOut(MenuItem item) {

		Intent login = new Intent(this, LoginActivity.class);
		this.startActivity(login);
		this.finish();

		Firebase ref = new Firebase(firebaseLoc);
		ref.unauth();
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

	/**
	 * Returns the user's list of projects
	 * 
	 * @return
	 */
	public ArrayList<Project> getProjects() {
		return this.user.getProjects();
	}

	/**
	 * Returns the Milestones for the selected Project
	 * 
	 * @return
	 */
	public ArrayList<Milestone> getMilestones() {
		return this.selectedProject.getMilestones();
	}
	
	public ArrayList<Task> getTasks() {
		return this.selectedMilestone.getTasks();
	}

	/**
	 * Returns the current user
	 * 
	 * @return
	 */
	public User getUser() {
		return this.user;
	}
}

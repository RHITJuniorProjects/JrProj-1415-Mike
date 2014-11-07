package rhit.jrProj.henry;

import java.util.ArrayList;
import java.util.Stack;

import rhit.jrProj.henry.firebase.Enums;
import rhit.jrProj.henry.firebase.Map;
import rhit.jrProj.henry.firebase.Member;
import rhit.jrProj.henry.firebase.Milestone;
import rhit.jrProj.henry.firebase.Project;
import rhit.jrProj.henry.firebase.Task;
import rhit.jrProj.henry.firebase.User;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;

public class MainActivity extends Activity implements
		ProjectListFragment.Callbacks, MilestoneListFragment.Callbacks,
		TaskListFragment.Callbacks, TaskDetailFragment.Callbacks {
	/**
	 * The Url to the firebase repository
	 */
	public final static String firebaseUrl = "https://henry-test.firebaseio.com/";
	/**
	 * If the application is in tablet mode or not.
	 */
	private boolean mTwoPane;

	/**
	 * Created user after login
	 */
	private User user;

	/**
	 * The project that has been selected from the list
	 */
	private Project selectedProject;

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
	private Stack<Fragment> fragmentStack;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Firebase.setAndroidContext(this);
		ActionBar actionBar = getActionBar();
		actionBar.setBackgroundDrawable(new ColorDrawable(0x268bd2));
		Firebase ref = new Firebase(firebaseUrl);
		this.fragmentStack = new Stack<Fragment>();
		AuthData authData = ref.getAuth();
		if (authData != null) {
			this.user = new User(firebaseUrl + "users/" + authData.getUid());
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
		args.putBoolean("TwoPane", this.mTwoPane);
		ProjectListFragment fragment = new ProjectListFragment();
		this.fragmentStack.push(fragment);
		getFragmentManager().beginTransaction().add(fragment, "Project_List")
				.addToBackStack("Project_List");
		fragment.setArguments(args);
		if (!this.mTwoPane) {
			setContentView(R.layout.activity_onepane);
			getFragmentManager().beginTransaction()
					.add(R.id.main_fragment_container, fragment).commit();

		} else {
			setContentView(R.layout.activity_twopane);
			getFragmentManager().beginTransaction()
					.add(R.id.twopane_list, fragment).commit();
		}
	}

	/**
	 * Creates the menu for the project
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.project, menu);

		// This code hides the "Create Milestone" and "Create Task" options when
		// viewing projects.
		MenuItem createMilestone = menu.findItem(R.id.action_milestone);
		createMilestone.setVisible(false);
		createMilestone.setEnabled(false);

		MenuItem createTask = menu.findItem(R.id.action_task);
		createTask.setVisible(false);
		createTask.setEnabled(false);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == android.R.id.home && this.fragmentStack.size() > 1) {
			this.fragmentStack.pop();
			Fragment beforeFragment = this.fragmentStack.peek();
			getActionBar().setDisplayHomeAsUpEnabled(
					this.fragmentStack.size() > 1);
			if (this.mTwoPane) {
				getFragmentManager().beginTransaction()
						.replace(R.id.twopane_list, beforeFragment).commit();
				Fragment fragmentID = getFragmentManager().findFragmentById(
						R.id.twopane_detail_container);
				if (fragmentID != null) {
					getFragmentManager().beginTransaction().remove(fragmentID)
							.commit();
				}

			} else {
				getFragmentManager().beginTransaction()
						.replace(R.id.main_fragment_container, beforeFragment)
						.commit();
			}
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
		this.fragmentStack.push(fragment);
		fragment.setArguments(args);
		getFragmentManager().beginTransaction().replace(container, fragment)
				.commit();
		if (this.mTwoPane) {
			getFragmentManager()
					.beginTransaction()
					.remove(getFragmentManager().findFragmentById(
							R.id.twopane_detail_container)).commit();
		}
		getActionBar().setDisplayHomeAsUpEnabled(true);
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
		this.fragmentStack.push(fragment);
		getFragmentManager().beginTransaction().add(fragment, "Milestone_List")
				.addToBackStack("Milestone_List");
		fragment.setArguments(args);
		getFragmentManager().beginTransaction().replace(container, fragment)
				.commit();
		if (this.mTwoPane) {
			getFragmentManager()
					.beginTransaction()
					.remove(getFragmentManager().findFragmentById(
							R.id.twopane_detail_container)).commit();
		}
		getActionBar().setDisplayHomeAsUpEnabled(true);
	}

	/**
	 * Open the MilestoneList Activity for the selected milestone
	 * 
	 * @param view
	 */
	public void openProjectView(View view) {
		int container = this.mTwoPane ? R.id.twopane_list
				: R.id.main_fragment_container;
		Bundle args = new Bundle();
		args.putBoolean("TwoPane", this.mTwoPane);
		ProjectListFragment fragment = new ProjectListFragment();
		this.fragmentStack.push(fragment);
		getFragmentManager().beginTransaction().add(fragment, "Project_View")
				.addToBackStack("Project_View");
		fragment.setArguments(args);
		getFragmentManager().beginTransaction().replace(container, fragment)
				.commit();
		if (this.mTwoPane) {
			getFragmentManager()
					.beginTransaction()
					.remove(getFragmentManager().findFragmentById(
							R.id.twopane_detail_container)).commit();
		}
		getActionBar().setDisplayHomeAsUpEnabled(false);
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
		// If in two pane mode, we cannot go up.

		if (!this.mTwoPane) {
			this.fragmentStack.push(fragment);
		}
		getActionBar().setDisplayHomeAsUpEnabled(!this.mTwoPane);
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
		if (!this.mTwoPane) {
			this.fragmentStack.push(fragment);
		}
		getActionBar().setDisplayHomeAsUpEnabled(true);
	}

	/**
	 * Callback method from {@link ItemListFragment.Callbacks} indicating that
	 * the item with the given ID was selected.
	 */
	public void onItemSelected(Task t) {
		this.selectedTask = t;
		Bundle arguments = new Bundle();
		arguments.putBoolean("Two Pane", this.mTwoPane);
		TaskDetailFragment fragment = new TaskDetailFragment();
		fragment.setArguments(arguments);
		getFragmentManager()
				.beginTransaction()
				.replace(
						this.mTwoPane ? R.id.twopane_detail_container
								: R.id.main_fragment_container, fragment)
				.commit();
		if (!this.mTwoPane) {
			this.fragmentStack.push(fragment);
		}
		getActionBar().setDisplayHomeAsUpEnabled(true);
	}

	/**
	 * Logout the user.
	 */
	public void logOut(MenuItem item) {

		Intent login = new Intent(this, LoginActivity.class);
		this.startActivity(login);
		this.finish();
		Firebase ref = new Firebase(firebaseUrl);
		ref.unauth();
	}

	/**
	 * Allows the project manager to create a new milestone.
	 */
	public void createMilestone(MenuItem item) {
		if (this.selectedProject != null
				&& this.selectedProject.getProjectId() != null) {

			CreateMilestoneFragment msFrag = new CreateMilestoneFragment();

			Bundle arguments = new Bundle();
			arguments.putString("projectid",
					this.selectedProject.getProjectId());
			msFrag.setArguments(arguments);
			msFrag.show(getFragmentManager(), "Diag");
		}

	}

	/**
	 * Allows the project manager to create a new task.
	 */
	public void createTask(MenuItem item) {

		if (this.selectedMilestone != null
				&& this.selectedMilestone.getMilestoneId() != null) {
			CreateTaskFragment taskFrag = new CreateTaskFragment();
			Bundle arguments = new Bundle();
			arguments.putString("milestoneId",
					this.selectedMilestone.getMilestoneId());
			arguments.putString("projectId",
					this.selectedProject.getProjectId());
			taskFrag.setArguments(arguments);
			taskFrag.show(getFragmentManager(), "Diag");
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

	/**
	 * Returns the Tasks for the selected Milestone
	 * 
	 * @return
	 */
	public ArrayList<Task> getTasks() {
		return this.selectedMilestone.getTasks();
	}

	/**
	 * Returns the Tasks for the selected Milestone
	 * 
	 * @return
	 */
	public Map<Member, Enums.Role> getProjectMembers() {
		return this.selectedProject.getMembers();
	}

	/**
	 * Returns the current user
	 * 
	 * @return
	 */
	public User getUser() {
		return this.user;
	}

	@Override
	public Project getSelectedProject() {
		return this.selectedProject;
	}
	
	/**
	 * Returns the currently selected task.
	 */
	public Task getSelectedTask(){
		return this.selectedTask;
	}
}
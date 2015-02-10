package rhit.jrProj.henry;

import java.util.ArrayList;
import java.util.Stack;

import rhit.jrProj.henry.firebase.Bounty;
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
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;

public class MainActivity extends Activity implements
		ProjectListFragment.Callbacks, MilestoneListFragment.Callbacks,
		ProjectMembersFragment.Callbacks, TaskListFragment.Callbacks,
		TaskDetailFragment.Callbacks, TasksAllListFragment.Callbacks,
		BountyListFragment.Callbacks,
		ProjectDetailFragment.Callbacks, ChartsFragment.Callbacks,
		MilestoneDetailFragment.Callbacks, BountyDetailFragment.Callbacks {

	/**
	 * The project that has been selected from the list
	 */
	private static Project selectedProject;

	/**
	 * The milestone selected by the user
	 */
	private static Milestone selectedMilestone;

	private Menu actionBarmenu;
	/**
	 * The task that is currently selected by the user
	 */
	private static Task selectedTask;
	/**
	 * The bounty that has been selected from the list
	 */
	private static Bounty selectedBounty;

	public static int DENSITY;
	/**
	 * Determines what page to fill in when the application starts
	 */
	private static Stack<Fragment> fragmentStack;

	/**
	 * sorting mode
	 */
	private static String sortingMode;

	/**
	 * current Fragment Used when the sorting mode is changed so that we can
	 * update the correct fragment's list.
	 */
	private static Fragment currFragment;

	private GlobalVariables mGlobalVariables;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		int density = metrics.densityDpi;
//		Log.i("flag", ((Integer) density).toString());
		DENSITY = density;
		mGlobalVariables = ((GlobalVariables) getApplicationContext());
		
		ActionBar actionBar = getActionBar();
		actionBar.setBackgroundDrawable(new ColorDrawable(0x268bd2));

		Firebase ref = new Firebase(mGlobalVariables.getFirebaseUrl());

		AuthData authData = ref.getAuth();
		if (mGlobalVariables.getUser() != null) {
			// I've been rotated!!!!!
			resumeOnRotate();
		} else if (authData != null) {
			mGlobalVariables.setUser(new User(mGlobalVariables.getFirebaseUrl()
					+ "users/" + authData.getUid()));
			createProjectList();
		} else if (this.getIntent().getStringExtra("user") != null) {
			// If logged in get the user's project list
			this.fragmentStack = new Stack<Fragment>();
			mGlobalVariables.setUser(new User(this.getIntent().getStringExtra(
					"user")));
			createProjectList();
		} else {
			// Starts the LoginActivity if the user has not been logged in just
			// yet
			Intent intent = new Intent(this, LoginActivity.class);
			this.startActivity(intent);
			this.finish();
		}
		String s=mGlobalVariables.getFirebaseUrl()
				+ "users/" + authData.getUid();
		Log.i("User Info", s);

	}

	private void resumeOnRotate() {
		mGlobalVariables.setmTwoPane((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE);
		if (!mGlobalVariables.ismTwoPane()) {
			setContentView(R.layout.activity_onepane);
			getFragmentManager()
					.beginTransaction()
					.add(R.id.main_fragment_container,
							this.fragmentStack.peek()).commit();
			this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		} else {
			setContentView(R.layout.activity_twopane);
			// getFragmentManager().beginTransaction() .add(R.id.twopane_list,
			// this.fragmentStack.peek()).commit();
		}
		getActionBar().setDisplayHomeAsUpEnabled(this.fragmentStack.size() > 1);
	}

	/**
	 * Determines if this activity should operate in two pane mode and creates
	 * the fragment to display a list of projects.
	 */
	private void createProjectList() {
		mGlobalVariables.setmTwoPane((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE);
		this.fragmentStack = new Stack<Fragment>();
		Bundle args = new Bundle();
		args.putBoolean("TwoPane", mGlobalVariables.ismTwoPane());
		ProjectListFragment fragment = new ProjectListFragment();
		this.fragmentStack.push(fragment);
		getFragmentManager().beginTransaction().add(fragment, "Project_List")
				.addToBackStack("Project_List");
		fragment.setArguments(args);
		this.currFragment = fragment;
		if (!mGlobalVariables.ismTwoPane()) {
			setContentView(R.layout.activity_onepane);
			getFragmentManager().beginTransaction()
					.add(R.id.main_fragment_container, fragment).commit();
			this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
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
		getMenuInflater().inflate(R.menu.main, menu);
		this.actionBarmenu = menu;
		MenuItem allTasks=menu.findItem(R.id.action_all_tasks);
		allTasks.setVisible(true);
		allTasks.setEnabled(true);
		MenuItem search = menu.findItem(R.id.action_search);
		search.setEnabled(false);
		search.setVisible(false);
		MenuItem sorting = menu.findItem(R.id.action_sorting);
		MenuItem charts = menu.findItem(R.id.action_charts);
		charts.setEnabled(false);
		charts.setVisible(false);
		sorting.setEnabled(true);
		sorting.setVisible(true);
		MenuItem createMilestone = menu.findItem(R.id.action_milestone);
		createMilestone.setVisible(false);
		createMilestone.setEnabled(false);
		MenuItem createTask = menu.findItem(R.id.action_task);
		createTask.setVisible(false);
		createTask.setEnabled(false);
		MenuItem createbounty = menu.findItem(R.id.action_bounty);
		createbounty.setVisible(false);
		createbounty.setEnabled(false); 
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
			if (mGlobalVariables.ismTwoPane()) {
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
	public void openBountyView(View view) {
		MenuItem item = this.actionBarmenu.findItem(R.id.action_all_tasks);
		item.setVisible(true);
		int container = mGlobalVariables.ismTwoPane() ? R.id.twopane_list
				: R.id.main_fragment_container;
		Bundle args = new Bundle();
		args.putBoolean("TwoPane", mGlobalVariables.ismTwoPane());
		BountyListFragment fragment = new BountyListFragment();
		this.fragmentStack.push(fragment);
		fragment.setArguments(args);
		this.currFragment = fragment;
		getFragmentManager().beginTransaction().replace(container, fragment)
				.commit();
		if (mGlobalVariables.ismTwoPane()) {
			getFragmentManager()
					.beginTransaction()
					.remove(getFragmentManager().findFragmentById(
							R.id.twopane_detail_container)).commit();
		}
		getActionBar().setDisplayHomeAsUpEnabled(true);
	}

	/**
	 * 
	 * @param view
	 */
	public void openTaskView(View view) {
		MenuItem item = this.actionBarmenu.findItem(R.id.action_all_tasks);
		item.setVisible(true);
		int container = mGlobalVariables.ismTwoPane() ? R.id.twopane_list
				: R.id.main_fragment_container;
		Bundle args = new Bundle();
		args.putBoolean("TwoPane", mGlobalVariables.ismTwoPane());
		TaskListFragment fragment = new TaskListFragment();
		this.fragmentStack.push(fragment);
		fragment.setArguments(args);
		this.currFragment = fragment;
		getFragmentManager().beginTransaction().replace(container, fragment)
				.commit();
		if (mGlobalVariables.ismTwoPane()) {
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
		MenuItem item = this.actionBarmenu.findItem(R.id.action_all_tasks);
		item.setVisible(true);
		int container = mGlobalVariables.ismTwoPane() ? R.id.twopane_list
				: R.id.main_fragment_container;
		Bundle args = new Bundle();
		args.putBoolean("TwoPane", mGlobalVariables.ismTwoPane());
		MilestoneListFragment fragment = new MilestoneListFragment();
		this.fragmentStack.push(fragment);
		getFragmentManager().beginTransaction().add(fragment, "Milestone_List")
				.addToBackStack("Milestone_List");
		fragment.setArguments(args);
		this.currFragment = fragment;
		getFragmentManager().beginTransaction().replace(container, fragment)
				.commit();
		if (mGlobalVariables.ismTwoPane()) {
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
		MenuItem item = this.actionBarmenu.findItem(R.id.action_all_tasks);
		item.setVisible(true);
		int container = mGlobalVariables.ismTwoPane() ? R.id.twopane_list
				: R.id.main_fragment_container;
		Bundle args = new Bundle();
		args.putBoolean("TwoPane", mGlobalVariables.ismTwoPane());
		ProjectListFragment fragment = new ProjectListFragment();
		this.fragmentStack.push(fragment);
		getFragmentManager().beginTransaction().add(fragment, "Project_View")
				.addToBackStack("Project_View");
		fragment.setArguments(args);
		this.currFragment = fragment;
		getFragmentManager().beginTransaction().replace(container, fragment)
				.commit();
		if (mGlobalVariables.ismTwoPane()) {
			getFragmentManager()
					.beginTransaction()
					.remove(getFragmentManager().findFragmentById(
							R.id.twopane_detail_container)).commit();
		}
		getActionBar().setDisplayHomeAsUpEnabled(false);
	}

	public void openAllMyTasks(MenuItem item) {
		if (!(this.fragmentStack.peek() instanceof TasksAllListFragment)) {
			MenuItem item2 = this.actionBarmenu.findItem(R.id.action_all_tasks);
			item.setVisible(false);
			FragmentManager manager = getFragmentManager();
			FragmentTransaction frgTrans = manager.beginTransaction();
			manager.popBackStack();
			Bundle args = new Bundle();
			args.putBoolean("TwoPane", mGlobalVariables.ismTwoPane());
			TasksAllListFragment fragment = new TasksAllListFragment();
			this.fragmentStack.push(fragment);
			fragment.setArguments(args);
			this.currFragment = fragment;
			int container = mGlobalVariables.ismTwoPane() ? R.id.twopane_detail_container
					: R.id.main_fragment_container;
			frgTrans.replace(container, fragment);
			frgTrans.commit();
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	/**
	 * Open the MilestoneList Activity for the selected milestone
	 * 
	 * @param view
	 */
	public void openProjectMembersView(View view) {
		MenuItem item = this.actionBarmenu.findItem(R.id.action_all_tasks);
		item.setVisible(true);
		int container = mGlobalVariables.ismTwoPane() ? R.id.twopane_list
				: R.id.main_fragment_container;
		Bundle args = new Bundle();
		args.putBoolean("TwoPane", mGlobalVariables.ismTwoPane());
		ProjectMembersFragment fragment = new ProjectMembersFragment();
		this.fragmentStack.push(fragment);
		getFragmentManager().beginTransaction()
				.add(fragment, "Project_Users_View")
				.addToBackStack("Project_Users_View");
		fragment.setArguments(args);
		currFragment = fragment;
		getFragmentManager().beginTransaction().replace(container, fragment)
				.commit();
		if (mGlobalVariables.ismTwoPane()) {
			getFragmentManager()
					.beginTransaction()
					.remove(getFragmentManager().findFragmentById(
							R.id.twopane_detail_container)).commit();
		}
		getActionBar().setDisplayHomeAsUpEnabled(true);
	}

	/**
	 * Callback method from {@link ProjectListFragment.Callbacks} indicating
	 * that the item with the given ID was selected.
	 */
	public void onItemSelected(Project p) {
		MenuItem charts = this.actionBarmenu.findItem(R.id.action_charts);
		charts.setEnabled(true);
		charts.setVisible(true);
		MenuItem item = this.actionBarmenu.findItem(R.id.action_all_tasks);
		item.setVisible(false);

		this.selectedProject = p;
		Bundle arguments = new Bundle();
		ProjectDetailFragment fragment = new ProjectDetailFragment();
		arguments.putBoolean("TwoPane", mGlobalVariables.ismTwoPane());
		fragment.setArguments(arguments);
		// currFragment=fragment;
		getFragmentManager()
				.beginTransaction()
				.replace(
						mGlobalVariables.ismTwoPane() ? R.id.twopane_detail_container
								: R.id.main_fragment_container, fragment)
				.commit();
		// If in two pane mode, we cannot go up.

		if (!mGlobalVariables.ismTwoPane()) {
			this.fragmentStack.push(fragment);
		}
		getActionBar().setDisplayHomeAsUpEnabled(!mGlobalVariables.ismTwoPane());
	}

	/**
	 * Callback method from {@link MilestoneListFragment.Callbacks} indicating
	 * that the item with the given ID was selected.
	 */
	public void onItemSelected(Milestone m) {
		MenuItem item = this.actionBarmenu.findItem(R.id.action_all_tasks);
		item.setVisible(false);
		this.selectedMilestone = m;
		Bundle arguments = new Bundle();
		MilestoneDetailFragment fragment = new MilestoneDetailFragment();
		fragment.setArguments(arguments);
		// currFragment=fragment;
		getFragmentManager()
				.beginTransaction()
				.replace(
						mGlobalVariables.ismTwoPane() ? R.id.twopane_detail_container
								: R.id.main_fragment_container, fragment)
				.commit();
		if (!mGlobalVariables.ismTwoPane()) {
			this.fragmentStack.push(fragment);
		}
		getActionBar().setDisplayHomeAsUpEnabled(true);
	}

	/**
	 * Callback method from {@link ItemListFragment.Callbacks} indicating that
	 * the item with the given ID was selected.
	 */
	public void onItemSelected(Task t) {
		MenuItem item = this.actionBarmenu.findItem(R.id.action_all_tasks);
		item.setVisible(false);
		this.selectedTask = t;
		Bundle arguments = new Bundle();
		arguments.putBoolean("Two Pane", mGlobalVariables.ismTwoPane());
		TaskDetailFragment fragment = new TaskDetailFragment();
		fragment.setArguments(arguments);
		// currFragment=fragment;
		getFragmentManager()
				.beginTransaction()
				.replace(
						mGlobalVariables.ismTwoPane() ? R.id.twopane_detail_container
								: R.id.main_fragment_container, fragment)
				.commit();
		if (!mGlobalVariables.ismTwoPane()) {
			this.fragmentStack.push(fragment);
		}
		getActionBar().setDisplayHomeAsUpEnabled(true);
	}
	/**
	 * Callback method from {@link ItemListFragment.Callbacks} indicating that
	 * the item with the given ID was selected.
	 */
	public void onItemSelected(Bounty t) {
		MenuItem item = this.actionBarmenu.findItem(R.id.action_all_tasks);
		item.setVisible(false);
		this.selectedBounty = t;
		Bundle arguments = new Bundle();
		arguments.putBoolean("Two Pane", mGlobalVariables.ismTwoPane());
		BountyDetailFragment fragment = new BountyDetailFragment();
		fragment.setArguments(arguments);
		// currFragment=fragment;
		getFragmentManager()
				.beginTransaction()
				.replace(
						mGlobalVariables.ismTwoPane() ? R.id.twopane_detail_container
								: R.id.main_fragment_container, fragment)
				.commit();
		if (!mGlobalVariables.ismTwoPane()) {
			this.fragmentStack.push(fragment);
		}
		getActionBar().setDisplayHomeAsUpEnabled(true);
	}

	/**
	 * Logout the user.
	 */
	public void logOut(MenuItem item) {

		Intent login = new Intent(this, LoginActivity.class);
		this.currFragment = null;
		mGlobalVariables.setUser(null);
		this.startActivity(login);
		this.finish();
		Firebase ref = new Firebase(mGlobalVariables.getFirebaseUrl());
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
			// if (this.currFragment instanceof MilestoneListFragment){
			// ((MilestoneListFragment)this.currFragment).dataChanged();
			// }
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
			this.currFragment = taskFrag;
			taskFrag.show(getFragmentManager(), "Diag");
		}
	}
	/**
	 * Allows the project manager to create a new task.
	 */
	public void createBounty(MenuItem item) {

		if (this.selectedTask != null
				&& this.selectedTask.getTaskId() != null) {
			CreateBountyFragment bountyFrag = new CreateBountyFragment();
			Bundle arguments = new Bundle();
			arguments.putString("taskId",
					this.selectedTask.getTaskId());
			arguments.putString("milestoneId",
					this.selectedMilestone.getMilestoneId());
			arguments.putString("projectId",
					this.selectedProject.getProjectId());
			bountyFrag.setArguments(arguments);
			this.currFragment = bountyFrag;
			bountyFrag.show(getFragmentManager(), "Diag");
		}
	}
	/**
	 * Allows the project manager to create a new task.
	 */
//	public void createBounty(MenuItem item) {
//
//		if (this.selectedTask != null
//				&& this.selectedTask.getTaskId() != null) {
//			DatePickerFragment bountyFrag = new DatePickerFragment();
//			Bundle arguments = new Bundle();
//			arguments.putString("taskId",
//					this.selectedTask.getTaskId());
//			arguments.putString("milestoneId",
//					this.selectedMilestone.getMilestoneId());
//			arguments.putString("projectId",
//					this.selectedProject.getProjectId());
//			bountyFrag.setArguments(arguments);
//			this.currFragment = bountyFrag;
//			bountyFrag.show(getFragmentManager(), "Diag");
//		}
//	}

	/**
	 * Open the search page
	 * 
	 * @param item
	 */
	public void search(MenuItem item) {
		// Not yet implemented
	}

	public void charts(MenuItem item) {

		Bundle arguments = new Bundle();
		ChartsFragment fragment = new ChartsFragment();
		arguments.putBoolean("TwoPane", mGlobalVariables.ismTwoPane());
		fragment.setArguments(arguments);
		// currFragment=fragment;
		getFragmentManager()
				.beginTransaction()
				.replace(
						mGlobalVariables.ismTwoPane() ? R.id.twopane_detail_container
								: R.id.main_fragment_container, fragment)
				.commit();
		// If in two pane mode, we cannot go up.

		if (!mGlobalVariables.ismTwoPane()) {
			this.fragmentStack.push(fragment);
		}
		getActionBar().setDisplayHomeAsUpEnabled(!mGlobalVariables.ismTwoPane());

	}

	/**
	 * sets Sorting mode, and then calls the sortingChanged method on the
	 * current fragment
	 */
	public void sortingMode(MenuItem item) {
		this.sortingMode = item.getTitle().toString();
		if (this.currFragment != null) {
			if (this.currFragment instanceof ProjectListFragment) {
				((ProjectListFragment) this.currFragment).sortingChanged();
			}
			if (this.currFragment instanceof MilestoneListFragment) {
				((MilestoneListFragment) this.currFragment).sortingChanged();
			}
			if (this.currFragment instanceof TaskListFragment) {
				((TaskListFragment) this.currFragment).sortingChanged();
			}
			if (this.currFragment instanceof BountyListFragment) {
				((BountyListFragment) this.currFragment).sortingChanged();
			}
			if (this.currFragment instanceof TasksAllListFragment) {
				((TasksAllListFragment) this.currFragment).sortingChanged();
			}
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
		return mGlobalVariables.getUser().getProjects();
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
		return mGlobalVariables.getUser();
	}

	public Project getSelectedProject() {
		return this.selectedProject;
	}

	/**
	 * Returns the currently selected task.
	 */
	public Task getSelectedTask() {
		return this.selectedTask;
	}

	/**
	 * Returns the currently selected milestone
	 */
	public Milestone getSelectedMilestone() {
		return this.selectedMilestone;
	}

	/**
	 * Returns the current sorting mode
	 */
	public String getSortMode() {
		return this.sortingMode;
	}

	/**
	 * Returns the current user's name
	 * 
	 * @return
	 */
	public String getUserName() {
		return mGlobalVariables.getUser().getName();
	}

	public String getUserID() {
		return mGlobalVariables.getUser().getKey();
	}

	public void onItemSelected(Member m) {
		// do nothing
	}

	public ArrayList<Member> getMembers() {
		return getProjectMembers().getAllKeys();
	}

	@Override
	public Bounty getSelectedBounty() {
		return this.selectedBounty;
	}

	@Override
	public ArrayList<Bounty> getBounties() {
		// TODO Auto-generated method stub
		return this.selectedTask.getBounties();
	}

	@Override
	public boolean isFromMainActivity() {
		// TODO Auto-generated method stub
		return true;
	}

}

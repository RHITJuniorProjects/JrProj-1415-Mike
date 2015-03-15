package rhit.jrProj.henry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

import rhit.jrProj.henry.firebase.Bounty;
import rhit.jrProj.henry.firebase.Enums;
import rhit.jrProj.henry.firebase.Map;
import rhit.jrProj.henry.firebase.Member;
import rhit.jrProj.henry.firebase.Milestone;
import rhit.jrProj.henry.firebase.Project;
import rhit.jrProj.henry.firebase.Task;
import rhit.jrProj.henry.firebase.Trophy;
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
import android.view.SubMenu;
import android.view.View;

import com.firebase.client.AuthData;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

public class StoreActivity extends Activity implements TrophyDetailFragment.Callbacks, TrophyListFragment.Callbacks{

	
	private Menu actionBarmenu;
	/**
	 * The task that is currently selected by the user
	 */
	
	public static int DENSITY;
	/**
	 * Determines what page to fill in when the application starts
	 */
	private static Stack<Fragment> fragmentStack;

	/**
	 * sorting mode
	 */
	private static String sortingMode="Least Expensive First";
	
	private static Trophy selectedTrophy;
	private GlobalVariables mGlobalVariables;
	private HashMap<String, Trophy> trophyHM= new HashMap<String, Trophy>();
	
	private Fragment currFragment;

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
		Firebase trophiesRef=ref.child("trophies");
		trophiesRef.addChildEventListener(new ChildEventListener(){

			@Override
			public void onCancelled(FirebaseError arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onChildAdded(DataSnapshot arg0, String arg1) {
				updateTrophy(arg0);
				
			}

			@Override
			public void onChildChanged(DataSnapshot arg0, String arg1) {
				updateTrophy(arg0);
				
			}

			@Override
			public void onChildMoved(DataSnapshot arg0, String arg1) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onChildRemoved(DataSnapshot arg0) {
				// TODO Auto-generated method stub
				
			}
			
		});

		AuthData authData = ref.getAuth();
		if (mGlobalVariables.getUser() != null) {
			// I've been rotated!!!!!
			resumeOnRotate();
		} else if (authData != null) {
			mGlobalVariables.setUser(new User(mGlobalVariables.getFirebaseUrl()
					+ "users/" + authData.getUid()));
			createStoreList();
		} else if (this.getIntent().getStringExtra("user") != null) {
			// If logged in get the user's project list
			this.fragmentStack = new Stack<Fragment>();
			mGlobalVariables.setUser(new User(this.getIntent().getStringExtra(
					"user")));
			createStoreList();
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
	private void updateTrophy(DataSnapshot arg0){
		trophyHM.put(arg0.getKey(), new Trophy(arg0.getRef().toString()));
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
	 * Creates the menu for the project
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		this.actionBarmenu = menu;
		MenuItem allTasks=menu.findItem(R.id.action_all_tasks);
		allTasks.setVisible(false);
		allTasks.setEnabled(false);
		MenuItem search = menu.findItem(R.id.action_search);
		search.setEnabled(false);
		search.setVisible(false);
		MenuItem charts = menu.findItem(R.id.action_charts);
		charts.setEnabled(false);
		charts.setVisible(false);
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
	 * Determines if this activity should operate in two pane mode and creates
	 * the fragment to display a list of projects.
	 */
	private void createStoreList() {
		mGlobalVariables.setmTwoPane((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE);
		this.fragmentStack = new Stack<Fragment>();
		Bundle args = new Bundle();
		args.putBoolean("TwoPane", mGlobalVariables.ismTwoPane());
		TrophyListFragment fragment = new TrophyListFragment();
		this.fragmentStack.push(fragment);
		this.currFragment=fragment;
		getFragmentManager().beginTransaction().add(fragment, "Trophy_List")
				.addToBackStack("Trophy_List");
		fragment.setArguments(args);
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

	@Override
	public Trophy getSelectedTrophy() {
		
		return this.selectedTrophy;
	}

	@Override
	public int getAvailablePoints() {
		if (mGlobalVariables.getUser()!=null){
			return mGlobalVariables.getUser().getAvailablePoints();
		}
		return 0;
	}

	@Override
	public void onItemSelected(Trophy p) {
		this.selectedTrophy = p;
		Bundle arguments = new Bundle();
		arguments.putBoolean("TwoPane", mGlobalVariables.ismTwoPane());
		TrophyDetailFragment fragment = new TrophyDetailFragment();
		fragment.setArguments(arguments);
		currFragment=fragment;
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
	

	@Override
	public ArrayList<Trophy> getTrophies() {
		return (ArrayList<Trophy>) trophyHM.values();
	}

	@Override
	public User getUser() {
		return mGlobalVariables.getUser();
	}

	@Override
	public String getSortMode() {
		return this.sortingMode;
	}
	public void buyTrophy(View view){
		if (this.mGlobalVariables.getUser()!=null){
			User u=this.mGlobalVariables.getUser();
			u.changeAvailablePoints(-1*this.selectedTrophy.getCost());
			u.addTrophy(this.selectedTrophy);
			Firebase ref=new Firebase(this.mGlobalVariables.getFirebaseUrl()+"users/"+u.getKey()+"/trophies/").push();
            ref.setValue(this.selectedTrophy.getName());
		}
	}
	public void sortingMode(MenuItem item) {
		this.sortingMode = item.getTitle().toString();
		if (this.currFragment != null) {
			((TrophyListFragment) this.currFragment).sortingChanged();

		}
	}
	

}

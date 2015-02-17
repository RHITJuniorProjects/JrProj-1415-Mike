package rhit.jrProj.henry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rhit.jrProj.henry.bridge.SortedArrayAdapter;
import rhit.jrProj.henry.bridge.SortedListChangeNotifier;
import rhit.jrProj.henry.firebase.Enums;
import rhit.jrProj.henry.firebase.Member;
import rhit.jrProj.henry.firebase.Milestone;
import rhit.jrProj.henry.firebase.Project;
import rhit.jrProj.henry.firebase.Task;
import android.app.Activity;
import android.app.ListFragment;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.client.Firebase;

/**
 * A list fragment representing a list of Items. This fragment also supports
 * tablet devices by allowing list items to be given an 'activated' state upon
 * selection. This helps indicate which item is currently being viewed in a
 * {@link ItemDetailFragment}.
 * <p>
 * Activities containing this fragment MUST implement the {@link Callbacks}
 * interface.
 */
public class TaskListFragment extends ListFragment {
	private String sortMode="Sort A-Z";
	/**
	 * The serialization (saved instance state) Bundle key representing the
	 * activated item position. Only used on tablets.
	 */
	private static final String STATE_ACTIVATED_POSITION = "activated_position";

	/**
	 * The fragment's current callback object, which is notified of list item
	 * clicks.
	 */
	private Callbacks mCallbacks = sDummyCallbacks;

	/**
	 * The current activated item position. Only used on tablets.
	 */
	private int mActivatedPosition = ListView.INVALID_POSITION;

	private ArrayList<Task> tasks;
	
	private GlobalVariables mGlobalVariables;

	/**
	 * A callback interface that all activities containing this fragment must
	 * implement. This mechanism allows activities to be notified of item
	 * selections.
	 */
	public interface Callbacks {
		/**
		 * Callback for when an item has been selected.
		 */
		public void onItemSelected(Task t);

		public ArrayList<Task> getTasks();

		public Project getSelectedProject();
		
		public String getUserName();

		public String getSortMode();
		public Milestone getSelectedMilestone();
		
	}

	/**
	 * A dummy implementation of the {@link Callbacks} interface that does
	 * nothing. Used only when this fragment is not attached to an activity.
	 */
	private static Callbacks sDummyCallbacks = new Callbacks() {

		public void onItemSelected(Task t) {
			// do nothing
		}

		public ArrayList<Task> getTasks() {
			return null;
		}

		public Project getSelectedProject() {
			return null;
		}
		public Milestone getSelectedMilestone(){
			return null;
		}
		public String getUserName(){
			return "";
		}

		public String getSortMode() {
			// TODO Auto-generated method stub
			return null;
		}
		
		
	};

	/**
	 * 
	 * The wrapper class for the list's assignee.
	 *
	 * @author rockwotj.
	 *         Created Nov 7, 2014.
	 */
	private class Assignee {
		Task task;
		
		public Assignee(Task task)
		{
			this.task = task;
		}
		
		@Override
		public String toString() {
			return "Assigned to: " + this.task.getAssignedUserName();
		}
	}
	
	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public TaskListFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mGlobalVariables =  ((GlobalVariables) getActivity().getApplicationContext());
		setHasOptionsMenu(true);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {

		super.onActivityCreated(savedInstanceState);
		this.tasks = this.mCallbacks.getTasks();
		TextView textView = new TextView(this.getActivity().getBaseContext());
		textView.setTextSize(24);
		textView.setTextColor(R.color.light_blue);
		textView.setText("  Tasks in:");
		textView.setClickable(false);
		textView.setEnabled(false);
		textView.setPadding(16, 0, 16, 0);
		this.getListView().addHeaderView(textView, null, false);
		TextView textView2 = new TextView(this.getActivity().getBaseContext());
		textView2.setTextSize(18);
		textView2.setTextColor(R.color.light_blue);
		textView2.setText("Project: "+this.mCallbacks.getSelectedProject().getName()+"\nMilestone: "+this.mCallbacks.getSelectedMilestone().getName());
		textView2.setClickable(false);
		textView2.setEnabled(false);
		textView2.setPadding(16, 0, 16, 0);
		this.getListView().addHeaderView(textView2, null, false);
		
		//This still doesn't account for dynamically adding and removing tasks
		List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
		for (Task task : this.tasks) {
			Map<String, Object> datum = new HashMap<String, Object>(2);
			datum.put("title", task);
			datum.put("assignee", new Assignee(task));
			data.add(datum);
		}
		SortedArrayAdapter<Task> adapter = new SortedArrayAdapter<Task>(getActivity(),android.R.layout.simple_list_item_activated_2,
				android.R.id.text1, this.tasks, Enums.ObjectType.TASK, mCallbacks.getUserName(), true);
		SortedListChangeNotifier<Task> lcn = new SortedListChangeNotifier<Task>(adapter);

		for (Task t : this.tasks) {
			t.setListChangeNotifier(lcn);
		}
		setListAdapter(adapter);
		this.setActivateOnItemClick(this.getArguments().getBoolean("TwoPane"));
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		// Restore the previously serialized activated item position.
		if (savedInstanceState != null
				&& savedInstanceState.containsKey(STATE_ACTIVATED_POSITION)) {
			setActivatedPosition(savedInstanceState
					.getInt(STATE_ACTIVATED_POSITION));
		}
	}

	@Override
	public void onPrepareOptionsMenu(Menu menu) {
		super.onPrepareOptionsMenu(menu);
		MenuItem allTasks=menu.findItem(R.id.action_all_tasks);
		allTasks.setVisible(true);
		allTasks.setEnabled(true);

		// This code shows the "Create Task" option when
		// viewing tasks.
		MenuItem createMilestone = menu.findItem(R.id.action_milestone);
		createMilestone.setVisible(false);
		createMilestone.setEnabled(false);
		SubMenu submenu=menu.findItem(R.id.action_sorting).getSubMenu();
		MenuItem dateOldest= submenu.findItem(R.id.sortOldest);
		MenuItem dateNewest= submenu.findItem(R.id.sortNewest);
		dateOldest.setVisible(false);
		dateOldest.setEnabled(false);
		dateNewest.setVisible(false);
		dateNewest.setEnabled(false);

		Firebase ref = new Firebase(mGlobalVariables.getFirebaseUrl());
		Enums.Role role = this.mCallbacks
				.getSelectedProject()
				.getMembers()
				.getValue(
						new Member(ref.getRoot().toString() + "/users/"
								+ ref.getAuth().getUid()));

		if (role != null && role.equals(Enums.Role.LEAD)) {
			MenuItem createTask = menu.findItem(R.id.action_task);
			createTask.setVisible(true);
			createTask.setEnabled(true);
		}
		MenuItem createbounty = menu.findItem(R.id.action_bounty);
		createbounty.setVisible(false);
		createbounty.setEnabled(false); 
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		// Activities containing this fragment must implement its callbacks.
		if (!(activity instanceof Callbacks)) {
			throw new IllegalStateException(
					"Activity must implement fragment's callbacks.");
		}

		mCallbacks = (Callbacks) activity;
	}

	@Override
	public void onDetach() {
		super.onDetach();

		// Reset the active callbacks interface to the dummy implementation.
		mCallbacks = sDummyCallbacks;
	}

	@Override
	public void onListItemClick(ListView listView, View view, int position,
			long id) {
		super.onListItemClick(listView, view, position-2, id);

		// Notify the active callbacks interface (the activity, if the
		// fragment is attached to one) that an item has been selected.
		mCallbacks.onItemSelected(this.tasks.get(position-2));
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if (mActivatedPosition != ListView.INVALID_POSITION) {
			// Serialize and persist the activated item position.
			outState.putInt(STATE_ACTIVATED_POSITION, mActivatedPosition);
		}
	}

	/**
	 * Turns on activate-on-click mode. When this mode is on, list items will be
	 * given the 'activated' state when touched.
	 */
	public void setActivateOnItemClick(boolean activateOnItemClick) {
		// When setting CHOICE_MODE_SINGLE, ListView will automatically
		// give items the 'activated' state when touched.
		getListView().setChoiceMode(
				activateOnItemClick ? ListView.CHOICE_MODE_SINGLE
						: ListView.CHOICE_MODE_NONE);
	}

	private void setActivatedPosition(int position) {
		if (position == ListView.INVALID_POSITION) {
			getListView().setItemChecked(mActivatedPosition, false);
		} else {
			getListView().setItemChecked(position, true);
		}

		mActivatedPosition = position;
	}
	
	public void sortingChanged(){
		this.sortMode=this.mCallbacks.getSortMode();
		for (Task p : this.tasks){
			((SortedListChangeNotifier<Task>) p.getListChangeNotifier()).changeSorting(this.sortMode);
		}
	}

}

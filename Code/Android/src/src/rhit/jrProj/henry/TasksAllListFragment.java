package rhit.jrProj.henry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rhit.jrProj.henry.bridge.SortedArrayAdapter;
import rhit.jrProj.henry.bridge.SortedListChangeNotifier;
import rhit.jrProj.henry.firebase.Enums;
import rhit.jrProj.henry.firebase.Milestone;
import rhit.jrProj.henry.firebase.Project;
import rhit.jrProj.henry.firebase.Task;
import rhit.jrProj.henry.firebase.User;
import android.app.Activity;
import android.app.ListFragment;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

/**
 * A list fragment representing a list of Items. This fragment also supports
 * tablet devices by allowing list items to be given an 'activated' state upon
 * selection. This helps indicate which item is currently being viewed in a
 * {@link ItemDetailFragment}.
 * <p>
 * Activities containing this fragment MUST implement the {@link Callbacks}
 * interface.
 */
public class TasksAllListFragment extends ListFragment {
	private String sortMode = "A-Z";
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

	private String userID;

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

		public User getUser();

		public Milestone getSelectedMilestone();

		public String getSortMode();

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

		public User getUser() {
			return null;
		}

		public Milestone getSelectedMilestone() {
			return null;

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
	 * @author rockwotj. Created Nov 7, 2014.
	 */
	private class Assignee {
		Task task;

		public Assignee(Task task) {
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
	public TasksAllListFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setHasOptionsMenu(true);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {

		super.onActivityCreated(savedInstanceState);
		this.userID = this.mCallbacks.getUser().getKey();
		this.tasks = getAllMyTasks();

		// This still doesn't account for dynamically adding and removing tasks
		List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
		for (Task task : this.tasks) {
			Map<String, Object> datum = new HashMap<String, Object>(2);
			datum.put("title", task);
			datum.put("assignee", new Assignee(task));
			data.add(datum);
		}
		SortedArrayAdapter<Task> adapter = new SortedArrayAdapter<Task>(
				getActivity(), android.R.layout.simple_list_item_activated_2,
				android.R.id.text1, this.tasks, Enums.ObjectType.TASK,
				false);
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

		// This code shows the "Create Task" option when
		// viewing tasks.
		MenuItem createMilestone = menu.findItem(R.id.action_milestone);
		createMilestone.setVisible(false);
		createMilestone.setEnabled(false);

		
		
			MenuItem createTask = menu.findItem(R.id.action_task);
			createTask.setVisible(false);
			createTask.setEnabled(false);
			MenuItem sorting= menu.findItem(R.id.action_sorting);
			
			sorting.setEnabled(true);
			sorting.setVisible(true);
			SubMenu submenu=menu.findItem(R.id.action_sorting).getSubMenu();
			MenuItem dateOldest= submenu.findItem(R.id.sortOldest);
			MenuItem dateNewest= submenu.findItem(R.id.sortNewest);
			dateOldest.setVisible(false);
			dateOldest.setEnabled(false);
			dateNewest.setVisible(false);
			dateNewest.setEnabled(false);
		
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		// Activities containing this fragment must implement its callbacks.
		if (!(activity instanceof Callbacks)) {
			throw new IllegalStateException(
					"Activity must implement fragment's callbacks.");
		}

		this.mCallbacks = (Callbacks) activity;
	}

	@Override
	public void onDetach() {
		super.onDetach();

		// Reset the active callbacks interface to the dummy implementation.
		this.mCallbacks = sDummyCallbacks;
	}

	@Override
	public void onListItemClick(ListView listView, View view, int position,
			long id) {
//		super.onListItemClick(listView, view, position, id);
//
//		// Notify the active callbacks interface (the activity, if the
//		// fragment is attached to one) that an item has been selected.
//		this.mCallbacks.onItemSelected(this.tasks.get(position));
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if (this.mActivatedPosition != ListView.INVALID_POSITION) {
			// Serialize and persist the activated item position.
			outState.putInt(STATE_ACTIVATED_POSITION, this.mActivatedPosition);
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
			getListView().setItemChecked(this.mActivatedPosition, false);
		} else {
			getListView().setItemChecked(position, true);
		}

		this.mActivatedPosition = position;
	}

	private ArrayList<Task> getAllMyTasks() {
		TextView textView = new TextView(this.getActivity().getBaseContext());
		textView.setTextSize(24);
		textView.setTextColor(R.color.light_blue);
		ArrayList<Task> t = new ArrayList<Task>();
		ArrayList<Project> projects;
		ArrayList<Milestone> milestones;
		ArrayList<Task> t2;
		if (this.mCallbacks.getSelectedProject() == null) {
			textView.setText("  "+this.mCallbacks.getUser().getName()+"'s Tasks\n  All Projects");

			
			projects = this.mCallbacks.getUser()
					.getProjects();
			for (Project p : projects) {
				milestones = p.getMilestones();
				for (Milestone m : milestones) {
					t2= m.getTasks();
					for (Task task : t2){
						if (task.getAssignedUserId().equals(this.mCallbacks.getUser().getKey())){
							t.add(task);
						}
					}
				}
			}
		}
		else if (this.mCallbacks.getSelectedMilestone() == null){
			
			Project p = this.mCallbacks.getSelectedProject();
			textView.setText("  "+this.mCallbacks.getUser().getName()+"'s Tasks\n  All Milestones in "+p.getName());
			milestones = p.getMilestones();
			for (Milestone m : milestones) {
				t2= m.getTasks();
				for (Task task : t2){
					if (task.getAssignedUserId().equals(this.mCallbacks.getUser().getKey())){
						t.add(task);
					}
				}
			}
		}
		else{
			Project p = this.mCallbacks.getSelectedProject();
			Milestone m= this.mCallbacks.getSelectedMilestone();
			textView.setText("  "+this.mCallbacks.getUser().getName()+"'s Tasks\n  In "+m.getName()+" of "+p.getName());
			t2= m.getTasks();
			for (Task task : t2){
				if (task.getAssignedUserId().equals(this.mCallbacks.getUser().getKey())){
					t.add(task);
				}
			}
			
		}
		textView.setClickable(false);
		textView.setEnabled(false);
		this.getListView().addHeaderView(textView, null, false);
		return t;
	}
	public void sortingChanged(){
		this.sortMode=this.mCallbacks.getSortMode();
		for (Task p : this.tasks){
			((SortedListChangeNotifier<Task>) p.getListChangeNotifier()).changeSorting(this.sortMode);
		}
	}
	

}

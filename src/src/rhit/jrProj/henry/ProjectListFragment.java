package rhit.jrProj.henry;

import java.util.ArrayList;

import rhit.jrProj.henry.bridge.SortedArrayAdapter;
import rhit.jrProj.henry.bridge.SortedListChangeNotifier;
import rhit.jrProj.henry.firebase.Enums;
import rhit.jrProj.henry.firebase.Project;
import rhit.jrProj.henry.firebase.User;
import android.app.Activity;
import android.app.ListFragment;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

/**
 * A list fragment representing a list of Projects. This fragment also supports
 * tablet devices by allowing list items to be given an 'activated' state upon
 * selection. This helps indicate which item is currently being viewed in a
 * {@link ProjectDetailFragment}.
 * <p>
 * Activities containing this fragment MUST implement the {@link Callbacks}
 * interface.
 */
public class ProjectListFragment extends ListFragment {

	ArrayList<Project> projects;
	
	String sortMode="A-Z";

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

	/**
	 * A callback interface that all activities containing this fragment must
	 * implement. This mechanism allows activities to be notified of item
	 * selections.
	 */
	public interface Callbacks {
		/**
		 * Callback for when an item has been selected.
		 */
		public void onItemSelected(Project p);
		
		public ArrayList<Project> getProjects();
		
		public User getUser();
		
		public String getSortMode();

		public Project getSelectedProject();
	}

	/**
	 * A dummy implementation of the {@link Callbacks} interface that does
	 * nothing. Used only when this fragment is not attached to an activity.
	 */
	private static Callbacks sDummyCallbacks = new Callbacks() {

		public void onItemSelected(Project p) {
			// Do nothing
		}

		public ArrayList<Project> getProjects() {
			return null;
		}

		public User getUser() {
			return null;
		}
		public String getSortMode(){
			return "A-Z";
		}

		public Project getSelectedProject() {
			// TODO Auto-generated method stub
			return null;
		}
	};

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public ProjectListFragment() {
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		this.projects = this.mCallbacks.getProjects();
		this.sortMode = this.mCallbacks.getSortMode();
		
		
		SortedArrayAdapter<Project> arrayAdapter = new SortedArrayAdapter<Project>(
				getActivity(), android.R.layout.simple_list_item_activated_2,
				android.R.id.text1, this.projects, Enums.ObjectType.PROJECT);
		setListAdapter(arrayAdapter);
		
		SortedListChangeNotifier<Project> lcn = new SortedListChangeNotifier<Project>(
				arrayAdapter, this.sortMode);
		
		this.mCallbacks.getUser()
				.setListChangeNotifier(lcn);
		for (Project project : this.projects) {
			project.setListChangeNotifier(lcn);
		}
		this.setActivateOnItemClick(this.getArguments().getBoolean("TwoPane"));

	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		if (savedInstanceState != null
				&& savedInstanceState.containsKey(STATE_ACTIVATED_POSITION)) {
			setActivatedPosition(savedInstanceState
					.getInt(STATE_ACTIVATED_POSITION));
		}

	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if (!(activity instanceof Callbacks)) {
			throw new IllegalStateException(
					"Activity must implement fragment's callbacks.");
		}
		this.mCallbacks = (Callbacks) activity;
	}

	@Override
	public void onDetach() {
		super.onDetach();
		this.mCallbacks = sDummyCallbacks;
	}

	@Override
	public void onListItemClick(ListView listView, View view, int position,
			long id) {
		super.onListItemClick(listView, view, position, id);
		this.mCallbacks.onItemSelected(this.mCallbacks.getProjects().get(position));
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if (this.mActivatedPosition != AdapterView.INVALID_POSITION) {
			// Serialize and persist the activated item position.
			outState.putInt(STATE_ACTIVATED_POSITION, this.mActivatedPosition);
		}
	}
	@Override
	public void onPrepareOptionsMenu(Menu menu) {
		super.onPrepareOptionsMenu(menu);
		MenuItem search=menu.findItem(R.id.action_search);
		search.setEnabled(false);
		search.setVisible(false);
		MenuItem sorting= menu.findItem(R.id.action_sorting);
		
			sorting.setEnabled(true);
			sorting.setVisible(true);
					
		// This code shows the "Create Milestone" option when
		// viewing milestones.
				SubMenu submenu=menu.findItem(R.id.action_sorting).getSubMenu();
				MenuItem dateOldest= submenu.findItem(R.id.sortOldest);
				MenuItem dateNewest= submenu.findItem(R.id.sortNewest);
				MenuItem AZ= submenu.findItem(R.id.sortAZ);
				MenuItem ZA= submenu.findItem(R.id.sortZA);
				dateOldest.setVisible(true);
				dateOldest.setEnabled(true);
				dateNewest.setVisible(true);
				dateNewest.setEnabled(true);
				AZ.setVisible(true);
				AZ.setEnabled(true);
				ZA.setVisible(true);
				ZA.setEnabled(true);
				MenuItem createMilestone = menu.findItem(R.id.action_milestone);
				createMilestone.setVisible(false);
				createMilestone.setEnabled(false);
				MenuItem createTask = menu.findItem(R.id.action_task);
				createTask.setVisible(false);
				createTask.setEnabled(false);
				
					
					
		}
	
	

	/**
	 * Turns on activate-on-click mode. When this mode is on, list items will be
	 * given the 'activated' state when touched.
	 */
	public void setActivateOnItemClick(boolean activateOnItemClick) {
		getListView().setChoiceMode(
				activateOnItemClick ? AbsListView.CHOICE_MODE_SINGLE
						: AbsListView.CHOICE_MODE_NONE);
	}

	private void setActivatedPosition(int position) {
		if (position == AdapterView.INVALID_POSITION) {
			getListView().setItemChecked(this.mActivatedPosition, false);
		} else {
			getListView().setItemChecked(position, true);
		}
		this.mActivatedPosition = position;
	}
	/**
	 * Notifies the Projects that the sorting mode has changed
	 * and calls the changeSorting() method on their respective adapters.
	 */
	public void sortingChanged(){
		this.sortMode=this.mCallbacks.getSortMode();
		for (Project p : this.projects){
			((SortedListChangeNotifier<Project>) p.getListChangeNotifier()).changeSorting(this.sortMode);
		}
	}
}

package rhit.jrProj.henry;

import java.util.ArrayList;

import rhit.jrProj.henry.bridge.SortedArrayAdapter;
import rhit.jrProj.henry.bridge.SortedListChangeNotifier;
import rhit.jrProj.henry.firebase.Enums;
import rhit.jrProj.henry.firebase.Member;
import rhit.jrProj.henry.firebase.Milestone;
import rhit.jrProj.henry.firebase.Project;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ListFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.client.Firebase;

/**
 * A list fragment representing a list of Milestones. This fragment also
 * supports tablet devices by allowing list items to be given an 'activated'
 * state upon selection. This helps indicate which item is currently being
 * viewed in a {@link MilestoneDetailFragment}.
 * <p>
 * Activities containing this fragment MUST implement the {@link Callbacks}
 * interface.
 */
public class MilestoneListFragment extends ListFragment {

	private String sortMode = "Sort A-Z";

	private ArrayAdapter adapter;

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

	private ArrayList<Milestone> milestones;

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
		public void onItemSelected(Milestone m);

		public ArrayList<Milestone> getMilestones();

		public Project getSelectedProject();

		public String getSortMode();

	}

	/**
	 * A dummy implementation of the {@link Callbacks} interface that does
	 * nothing. Used only when this fragment is not attached to an activity.
	 */
	private static Callbacks sDummyCallbacks = new Callbacks() {
		public void onItemSelected(Milestone m) {
			// Do nothing
		}

		public ArrayList<Milestone> getMilestones() {
			return null;
		}

		public Project getSelectedProject() {
			return null;
		}

		public String getSortMode() {
			// TODO Auto-generated method stub
			return null;
		}

	};

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public MilestoneListFragment() {
	}

	/**
 * 
 */

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Done: replace with a real list adapter.
		mGlobalVariables = ((GlobalVariables) getActivity()
				.getApplicationContext());
		setHasOptionsMenu(true);
		this.milestones = this.mCallbacks.getMilestones();
		
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
		
		TextView textView = new TextView(this.getActivity().getBaseContext());
		textView.setTextSize(24);
		int lightGray = this.getResources().getColor(R.color.grey_font);
		textView.setTextColor(lightGray);
		textView.setText("Milestones in:");
		textView.setClickable(false);
		textView.setEnabled(false);
		textView.setPadding(16, 0, 16, 0);
		this.getListView().addHeaderView(textView, null, false);
		TextView textView2 = new TextView(this.getActivity().getBaseContext());
		textView2.setTextSize(18);
		textView2.setTextColor(lightGray);
		textView2.setText("Project: "
				+ this.mCallbacks.getSelectedProject().getName());
		textView2.setClickable(false);
		textView2.setEnabled(false);
		textView2.setPadding(16, 0, 16, 0);
		this.getListView().addHeaderView(textView2, null, false);

		SortedArrayAdapter<Milestone> arrayAdapter = new SortedArrayAdapter<Milestone>(
				getActivity(), android.R.layout.simple_list_item_activated_2,
				android.R.id.text1, this.milestones,
				Enums.ObjectType.MILESTONE, false);
		SortedListChangeNotifier<Milestone> lcn = new SortedListChangeNotifier<Milestone>(
				arrayAdapter);
		for (Milestone m : this.milestones) {
			m.setListChangeNotifier(lcn);
		}
		this.adapter = arrayAdapter;
		setListAdapter(arrayAdapter);
		this.setActivateOnItemClick(this.getArguments().getBoolean("TwoPane"));

	}

	@Override
	public void onPrepareOptionsMenu(Menu menu) {
		super.onPrepareOptionsMenu(menu);
		MenuItem allTasks=menu.findItem(R.id.action_all_tasks);
		allTasks.setVisible(false);
		allTasks.setEnabled(false);

		// This code shows the "Create Milestone" option when
		// viewing milestones.
		Firebase ref = new Firebase(mGlobalVariables.getFirebaseUrl());
		Enums.Role role = this.mCallbacks
				.getSelectedProject()
				.getMembers()
				.getValue(
						new Member(ref.getRoot().toString() + "/users/"
								+ ref.getAuth().getUid()));

		if (this.getArguments().getBoolean("TwoPane")) {
			if (role != null && role.equals(Enums.Role.LEAD)) {
				MenuItem createMilestone = menu.findItem(R.id.action_milestone);
				createMilestone.setVisible(true);
				createMilestone.setEnabled(true);
			}
		}
		MenuItem createbounty = menu.findItem(R.id.action_bounty);
		createbounty.setVisible(false);
		createbounty.setEnabled(false); 

	}
	
	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
		setListAdapter(null);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		// Activities containing this fragment must implement its call backs.
		if (!(activity instanceof Callbacks)) {
			throw new IllegalStateException(
					"Activity must implement fragment's callbacks.");
		}

		mCallbacks = (Callbacks) activity;
	}

	@Override
	public void onDetach() {
		super.onDetach();

		// Reset the active call backs interface to the dummy implementation.
		mCallbacks = sDummyCallbacks;
	}

	@Override
	public void onListItemClick(ListView listView, View view, int position,
			long id) {
		super.onListItemClick(listView, view, position - 2, id);

		// Notify the active call backs interface (the activity, if the
		// fragment is attached to one) that an item has been selected.
		mCallbacks.onItemSelected(this.milestones.get(position - 2));
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

	/**
	 * 
	 * @param position
	 */
	private void setActivatedPosition(int position) {
		if (position == ListView.INVALID_POSITION) {
			getListView().setItemChecked(mActivatedPosition, false);
		} else {
			getListView().setItemChecked(position, true);
		}

		mActivatedPosition = position;
	}

	public void sortingChanged() {
		this.sortMode = this.mCallbacks.getSortMode();
		for (Milestone p : this.milestones) {
			((SortedListChangeNotifier<Milestone>) p.getListChangeNotifier())
					.changeSorting(this.sortMode);
		}
	}

}
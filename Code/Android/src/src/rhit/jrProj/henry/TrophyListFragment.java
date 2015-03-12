package rhit.jrProj.henry;

import java.util.ArrayList;

import rhit.jrProj.henry.bridge.SortedArrayAdapter;
import rhit.jrProj.henry.bridge.SortedListChangeNotifier;
import rhit.jrProj.henry.firebase.Enums;
import rhit.jrProj.henry.firebase.Trophy;
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
 * A list fragment representing a list of Trophies. This fragment also supports
 * tablet devices by allowing list items to be given an 'activated' state upon
 * selection. This helps indicate which item is currently being viewed in a
 * {@link TrophyDetailFragment}.
 * <p>
 * Activities containing this fragment MUST implement the {@link Callbacks}
 * interface.
 */
public class TrophyListFragment extends ListFragment {

	ArrayList<Trophy> trophies;
	
	String sortMode="Sort A-Z";

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
		public void onItemSelected(Trophy p);
		
		public ArrayList<Trophy> getTrophies();
		
		public User getUser();
		
		public String getSortMode();

		public Trophy getSelectedTrophy();
	}

	/**
	 * A dummy implementation of the {@link Callbacks} interface that does
	 * nothing. Used only when this fragment is not attached to an activity.
	 */
	private static Callbacks sDummyCallbacks = new Callbacks() {

		public void onItemSelected(Trophy p) {
			// Do nothing
		}

		public ArrayList<Trophy> getTrophies() {
			return null;
		}

		public User getUser() {
			return null;
		}
		public String getSortMode(){
			return "A-Z";
		}

		public Trophy getSelectedTrophy() {
			// TODO Auto-generated method stub
			return null;
		}
	};

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public TrophyListFragment() {
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		this.trophies = this.mCallbacks.getTrophies();
		this.sortMode = this.mCallbacks.getSortMode();
		
		
		SortedArrayAdapter<Trophy> arrayAdapter = new SortedArrayAdapter<Trophy>(
				getActivity(), android.R.layout.simple_list_item_activated_2,
				android.R.id.text1, this.trophies, Enums.ObjectType.TROPHY, false);
		setListAdapter(arrayAdapter);
		
		SortedListChangeNotifier<Trophy> lcn = new SortedListChangeNotifier<Trophy>(
				arrayAdapter, this.sortMode);
		
		this.mCallbacks.getUser()
				.setTrophyListChangeNotifier(lcn);
		for (Trophy trophy : this.trophies) {
			trophy.setListChangeNotifier(lcn);
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
		this.mCallbacks.onItemSelected(this.mCallbacks.getTrophies().get(position));
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
				dateOldest.setVisible(false);
				dateOldest.setEnabled(false);
				dateNewest.setVisible(false);
				dateNewest.setEnabled(false);
				AZ.setVisible(true);
				AZ.setEnabled(true);
				ZA.setVisible(true);
				ZA.setEnabled(true);
				MenuItem lowest= submenu.findItem(R.id.sortLowest);
				MenuItem highest= submenu.findItem(R.id.sortHighest);
				lowest.setEnabled(true);
				lowest.setVisible(true);
				highest.setEnabled(true);
				highest.setVisible(true);
					
					
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
	 * Notifies the Trophies that the sorting mode has changed
	 * and calls the changeSorting() method on their respective adapters.
	 */
	public void sortingChanged(){
		this.sortMode=this.mCallbacks.getSortMode();
		for (Trophy p : this.trophies){
			((SortedListChangeNotifier<Trophy>) p.getListChangeNotifier()).changeSorting(this.sortMode);
		}
	}
}
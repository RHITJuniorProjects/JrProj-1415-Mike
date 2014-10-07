package rhit.jrProj.henry;

import rhit.jrProj.henry.firebase.Milestone;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

/**
 * An activity representing a list of Milestones. This activity has different
 * presentations for handset and tablet-size devices. On handsets, the activity
 * presents a list of items, which when touched, lead to a
 * {@link MilestoneDetailActivity} representing item details. On tablets, the
 * activity presents the list of items and item details side-by-side using two
 * vertical panes.
 * <p>
 * The activity makes heavy use of fragments. The list of items is a
 * {@link MilestoneListFragment} and the item details (if present) is a
 * {@link MilestoneDetailFragment}.
 * <p>
 * This activity also implements the required
 * {@link MilestoneListFragment.Callbacks} interface to listen for item
 * selections.
 */
public class MilestoneListActivity extends Activity implements
		MilestoneListFragment.Callbacks {

	/**
	 * Whether or not the activity is in two-pane mode, i.e. running on a tablet
	 * device.
	 */
	private boolean mTwoPane;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		FrameLayout layout = new FrameLayout(this);
		layout.setId(0x1111);
		setContentView(layout);
		// Show the Up button in the action bar.
		Bundle args = new Bundle();
		args.putParcelableArrayList("Milestones", this.getIntent().getParcelableArrayListExtra("Milestones"));
		FragmentTransaction t = getFragmentManager().beginTransaction();
		MilestoneListFragment frag = new MilestoneListFragment();
		frag.setArguments(args);
		t.add(layout.getId(), frag, "Milestone List");
		t.commit();
		// Template code
		getActionBar().setDisplayHomeAsUpEnabled(true);

		if (findViewById(R.id.milestone_detail_container) != null) {
			// The detail container view will be present only in the
			// large-screen layouts (res/values-large and
			// res/values-sw600dp). If this view is present, then the
			// activity should be in two-pane mode.
			this.mTwoPane = true;

			// In two-pane mode, list items should be given the
			// 'activated' state when touched.
			((MilestoneListFragment) getFragmentManager().findFragmentById(
					R.id.milestone_list)).setActivateOnItemClick(true);
		}

		// TODO: If exposing deep links into your app, handle intents here.
		//Intent intent = this.getIntent();
	//	ArrayList<Milestones> milestoneList = intent.getParcelableArrayListExtra(name);
		
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == android.R.id.home) {
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * Callback method from {@link MilestoneListFragment.Callbacks} indicating
	 * that the item with the given ID was selected.
	 */
	public void onItemSelected(Milestone m) {
		if (this.mTwoPane) {
			// In two-pane mode, show the detail view in this activity by
			// adding or replacing the detail fragment using a
			// fragment transaction.
			Bundle arguments = new Bundle();
			//arguments.putString(MilestoneDetailFragment.ARG_ITEM_ID, id);
			arguments.putParcelable("Milestone", m);
			MilestoneDetailFragment fragment = new MilestoneDetailFragment();
			fragment.setArguments(arguments);
			getFragmentManager().beginTransaction()
					.replace(R.id.milestone_detail_container, fragment)
					.commit();

		} else {
			// In single-pane mode, simply start the detail activity
			// for the selected item ID.
			Intent detailIntent = new Intent(this,
					MilestoneDetailActivity.class);
			detailIntent.putExtra("Milestone", m);
			startActivity(detailIntent);
		}
	}
}

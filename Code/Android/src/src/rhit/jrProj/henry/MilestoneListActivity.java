package rhit.jrProj.henry;

import java.util.ArrayList;

import rhit.jrProj.henry.firebase.Milestone;
import rhit.jrProj.henry.firebase.Project;
import rhit.jrProj.henry.firebase.Task;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

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

	/**
	 * The list of milestones
	 */
	private ArrayList<Milestone> milestones;

	private Milestone milestoneItem;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.milestones = this.getIntent().getParcelableArrayListExtra(
				"Milestones");

		boolean tabletSize = (getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
		if (!tabletSize) {
			setContentView(R.layout.activity_milestone_list);
		} else {
			setContentView(R.layout.activity_milestone_twopane);
		}
		getActionBar().setDisplayHomeAsUpEnabled(true);
		if (findViewById(R.id.milestone_detail_container) != null) {
			this.mTwoPane = true;
			((MilestoneListFragment) getFragmentManager().findFragmentById(
					R.id.milestone_list)).setActivateOnItemClick(true);
		}
	}

	/**
	 * Returns the list of milestones
	 * 
	 * @return
	 */
	public ArrayList<Milestone> getMilestones() {
		return this.milestones;
	}

	/**
	 * 
	 * @param view
	 */
	public void openTaskView(View view) {
		Intent intent = new Intent(this, TaskListActivity.class);
		ArrayList<Task> tasks = this.milestoneItem.getTasks();
		intent.putParcelableArrayListExtra("Tasks", tasks);
		this.startActivity(intent);
	}

	/**
 * 
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
	 * Callback method from {@link MilestoneListFragment.Callbacks} indicating
	 * that the item with the given ID was selected.
	 */
	public void onItemSelected(Milestone m) {
		this.milestoneItem = m;
		if (this.mTwoPane) {
			Bundle arguments = new Bundle();
			arguments.putParcelable("Milestone", m);
			MilestoneDetailFragment fragment = new MilestoneDetailFragment();
			fragment.setArguments(arguments);
			getFragmentManager().beginTransaction()
					.replace(R.id.milestone_detail_container, fragment)
					.commit();

		} else {
			Intent detailIntent = new Intent(this,
					MilestoneDetailActivity.class);
			detailIntent.putExtra("Milestone", m);
			startActivity(detailIntent);
		}
	}
}

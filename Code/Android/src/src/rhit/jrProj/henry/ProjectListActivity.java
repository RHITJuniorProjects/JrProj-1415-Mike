package rhit.jrProj.henry;

import rhit.jrProj.henry.firebase.Project;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.widget.FrameLayout;

/**
 * An activity representing a list of Projects. This activity has different
 * presentations for handset and tablet-size devices. On handsets, the activity
 * presents a list of items, which when touched, lead to a
 * {@link ProjectDetailActivity} representing item details. On tablets, the
 * activity presents the list of items and item details side-by-side using two
 * vertical panes.
 * <p>
 * The activity makes heavy use of fragments. The list of items is a
 * {@link ProjectListFragment} and the item details (if present) is a
 * {@link ProjectDetailFragment}.
 * <p>
 * This activity also implements the required
 * {@link ProjectListFragment.Callbacks} interface to listen for item
 * selections.
 */
public class ProjectListActivity extends Activity implements
		ProjectListFragment.Callbacks {

	/**
	 * Whether or not the activity is in two-pane mode, i.e. running on a tablet
	 * device.
	 */
	private boolean mTwoPane;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		FrameLayout layout = new FrameLayout(this);
		layout.setId(0x4774);
		setContentView(layout);
		// Create the parameters for the Project List Fragment and then display/create it
		Bundle arguments = new Bundle();
		arguments.putParcelableArrayList("Projects", this.getIntent().getParcelableArrayListExtra("Projects"));
		FragmentTransaction t = getFragmentManager().beginTransaction();
		ProjectListFragment fragment = new ProjectListFragment();
		fragment.setArguments(arguments);
		t.add(layout.getId(), fragment, "Project List");
		t.commit();
		// Show the Up button in the action bar.
		getActionBar().setDisplayHomeAsUpEnabled(true);
		if (findViewById(R.id.project_detail_container) != null) {
			// The detail container view will be present only in the
			// large-screen layouts (res/values-large and
			// res/values-sw600dp). If this view is present, then the
			// activity should be in two-pane mode.
			this.mTwoPane = true;

			// In two-pane mode, list items should be given the
			// 'activated' state when touched.
			((ProjectListFragment) getFragmentManager().findFragmentById(
					R.id.project_list)).setActivateOnItemClick(true);
		}
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
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * Callback method from {@link ProjectListFragment.Callbacks} indicating
	 * that the item with the given ID was selected.
	 */
	public void onItemSelected(Project p) {
		if (this.mTwoPane) {
			// In two-pane mode, show the detail view in this activity by
			// adding or replacing the detail fragment using a
			// fragment transaction.
			Bundle arguments = new Bundle();
			//arguments.putString(ProjectDetailFragment.ARG_ITEM_ID, id);
			arguments.putParcelable("Project", p);
			ProjectDetailFragment fragment = new ProjectDetailFragment();
			fragment.setArguments(arguments);
			getFragmentManager().beginTransaction()
					.replace(R.id.project_detail_container, fragment)
					.commit();

		} else {
			// In single-pane mode, simply start the detail activity
			// for the selected item ID.
			Intent detailIntent = new Intent(this,
					ProjectDetailActivity.class);
			//detailIntent.putExtra(ProjectDetailFragment.ARG_ITEM_ID, id);
			detailIntent.putExtra("Project", p);
			startActivity(detailIntent);
		}
	}
}

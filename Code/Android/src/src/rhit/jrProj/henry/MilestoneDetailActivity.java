package rhit.jrProj.henry;

import java.util.ArrayList;

import rhit.jrProj.henry.firebase.Milestone;
import rhit.jrProj.henry.firebase.Task;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

/**
 * An activity representing a single Milestone detail screen. This activity is
 * only used on handset devices. On tablet-size devices, item details are
 * presented side-by-side with a list of items in a
 * {@link MilestoneListActivity}.
 * <p>
 * This activity is mostly just a 'shell' activity containing nothing more than
 * a {@link MilestoneDetailFragment}.
 */
public class MilestoneDetailActivity extends Activity {

	private Milestone milestoneItem;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_milestone_detail);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		if (savedInstanceState == null) {
			Bundle arguments = new Bundle();
			this.milestoneItem = getIntent().getParcelableExtra("Milestone");
			arguments.putParcelable("Milestone", this.milestoneItem);
			MilestoneDetailFragment fragment = new MilestoneDetailFragment();
			fragment.setArguments(arguments);
			getFragmentManager().beginTransaction()
					.add(R.id.milestone_detail_container, fragment).commit();
		}
	}

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
	 * The method that is called when the "View Tasks" button is pressed.
	 * 
	 * @param view
	 */
	public void openTaskView(View view) {
		Intent intent = new Intent(this, TaskListActivity.class);
		ArrayList<Task> tasks = this.milestoneItem.getTasks();
		intent.putParcelableArrayListExtra("Tasks", tasks);
		this.startActivity(intent);
	}
}

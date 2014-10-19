package rhit.jrProj.henry;

import rhit.jrProj.henry.firebase.Task;
import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;

/**
 * An activity representing a single Item detail screen. This activity is only
 * used on handset devices. On tablet-size devices, item details are presented
 * side-by-side with a list of items in a {@link ItemListActivity}.
 * <p>
 * This activity is mostly just a 'shell' activity containing nothing more than
 * a {@link ItemDetailFragment}.
 */
public class TaskDetailActivity extends Activity {
	
	private Task taskItem;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_task_detail);

		// Show the Up button in the action bar.
		getActionBar().setDisplayHomeAsUpEnabled(true);
		if (savedInstanceState == null) {
			Bundle arguments = new Bundle();
			this.taskItem = getIntent().getParcelableExtra("Task");
			arguments.putParcelable("Task", this.taskItem);
			TaskDetailFragment fragment = new TaskDetailFragment();
			fragment.setContext(this);
			fragment.setArguments(arguments);
			getFragmentManager().beginTransaction()
				.add(R.id.task_detail_container, fragment).commit();
			/*arguments.putString(TaskDetailFragment.ARG_ITEM_ID, getIntent()
					.getStringExtra(TaskDetailFragment.ARG_ITEM_ID));
			TaskDetailFragment fragment = new TaskDetailFragment();
			fragment.setArguments(arguments);*/
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
}

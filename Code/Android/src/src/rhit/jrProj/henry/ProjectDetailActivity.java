package rhit.jrProj.henry;

import java.util.ArrayList;

import rhit.jrProj.henry.firebase.Milestone;
import rhit.jrProj.henry.firebase.Project;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.view.View;

/**
 * An activity representing a single Project detail screen. This activity is
 * only used on handset devices. On tablet-size devices, item details are
 * presented side-by-side with a list of items in a {@link ProjectListActivity}.
 * <p>
 * This activity is mostly just a 'shell' activity containing nothing more than
 * a {@link ProjectDetailFragment}.
 */
public class ProjectDetailActivity extends Activity {

	private Project projectItem;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_project_detail);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		if (savedInstanceState == null) {
			Bundle arguments = new Bundle();
			this.projectItem = getIntent().getParcelableExtra("Project");
			arguments.putParcelable("Project", projectItem);
			ProjectDetailFragment fragment = new ProjectDetailFragment();
			fragment.setArguments(arguments);
			getFragmentManager().beginTransaction()
					.add(R.id.project_detail_container, fragment).commit();
		}
	}

	/**
	 * Opens a Milestone list activity and lists all the milestones for the selected project
	 * 
	 * @param view
	 */
	public void openMilestoneView(View view) {
		Intent intent = new Intent(this, MilestoneListActivity.class);
		ArrayList<Milestone> milestones = this.projectItem.getMilestones();
		intent.putParcelableArrayListExtra("Milestones", milestones);
		this.startActivity(intent);
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
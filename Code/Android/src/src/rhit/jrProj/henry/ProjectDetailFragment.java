package rhit.jrProj.henry;

import rhit.jrProj.henry.firebase.Project;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * A fragment representing a single Project detail screen. This fragment is
 * either contained in a {@link ProjectListActivity} in two-pane mode (on
 * tablets) or a {@link ProjectDetailActivity} on handsets.
 */
public class ProjectDetailFragment extends Fragment {

	/**
	 * The dummy content this fragment is presenting.
	 */
	private Project projectItem;

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public ProjectDetailFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (getArguments().containsKey("Project")) {
			// Load the dummy content specified by the fragment
			// arguments. In a real-world scenario, use a Loader
			// to load content from a content provider.
			this.projectItem = (Project) getArguments()
					.getParcelable("Project");
		}

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_project_detail,
				container, false);
		if (this.projectItem != null) {
			((TextView) rootView.findViewById(R.id.project_name))
				.setText(this.projectItem.getName());
			((TextView) rootView.findViewById(R.id.project_due_date))
				.setText(this.projectItem.getDueDate());
			((TextView) rootView.findViewById(R.id.project_description))
				.setText(this.projectItem.getDescription());
			
			//Progress Bars for Hours, Tasks, and Milestones
			((TextView) rootView.findViewById(R.id.project_hours_percent))
				.setText("Hours Completed: " + this.projectItem.getHoursPercent() + "%");
			ProgressBar hoursCompleteBar = ((ProgressBar) rootView.findViewById(R.id.project_hours_progress_bar));
			hoursCompleteBar.setMax(100);
			hoursCompleteBar.setProgress(this.projectItem.getHoursPercent());
			
			((TextView) rootView.findViewById(R.id.project_tasks_percent))
				.setText("Tasks Completed: " + this.projectItem.getTasksPercent() + "%");
			ProgressBar tasksCompleteBar = ((ProgressBar) rootView.findViewById(R.id.project_tasks_progress_bar));
			tasksCompleteBar.setMax(100);
			tasksCompleteBar.setProgress(this.projectItem.getTasksPercent());
			
			((TextView) rootView.findViewById(R.id.project_milestones_percent))
				.setText("Milestones Completed: " + this.projectItem.getMilestonesPercent() + "%");
			ProgressBar milestonesCompleteBar = ((ProgressBar) rootView.findViewById(R.id.project_milestones_progress_bar));
			milestonesCompleteBar.setMax(100);
			milestonesCompleteBar.setProgress(this.projectItem.getMilestonesPercent());
		}

		return rootView;
	}

}

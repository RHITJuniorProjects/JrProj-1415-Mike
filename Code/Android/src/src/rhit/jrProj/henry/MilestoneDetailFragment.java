package rhit.jrProj.henry;

import rhit.jrProj.henry.firebase.Milestone;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * A fragment representing a single Milestone detail screen. This fragment is
 * either contained in a {@link MilestoneListActivity} in two-pane mode (on
 * tablets) or a {@link MilestoneDetailActivity} on handsets.
 */
public class MilestoneDetailFragment extends Fragment {

	/**
	 * The dummy content this fragment is presenting.
	 */
	private Milestone milestoneItem;

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public MilestoneDetailFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (getArguments().containsKey("Milestone")) {
			this.milestoneItem = this.getArguments().getParcelable("Milestone");
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_milestone_detail,
				container, false);
		if (this.milestoneItem != null) {
			((TextView) rootView.findViewById(R.id.milestone_name))
				.setText(this.milestoneItem.getName());
			((TextView) rootView.findViewById(R.id.milestone_due_date))
				.setText(this.milestoneItem.getDueDate());
			((TextView) rootView.findViewById(R.id.milestone_description))
				.setText(this.milestoneItem.getDescription());
			
			((TextView) rootView.findViewById(R.id.milestone_task_percent))
				.setText("Tasks Completed: " + this.milestoneItem.getTaskPercent() + "%");
			ProgressBar taskCompleteBar = ((ProgressBar) rootView.findViewById(R.id.milestone_task_progress_bar));
			taskCompleteBar.setMax(100);
			taskCompleteBar.setProgress(this.milestoneItem.getTaskPercent());
			
			
		}

		return rootView;
	}
	
	
}

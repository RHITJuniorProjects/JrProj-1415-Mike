package rhit.jrProj.henry;

import rhit.jrProj.henry.firebase.Task;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

//import com.example.firebasetest.List.ListContent;

/**
 * A fragment representing a single Item detail screen. This fragment is either
 * contained in a {@link ItemListActivity} in two-pane mode (on tablets) or a
 * {@link ItemDetailActivity} on handsets.
 */
public class TaskDetailFragment extends Fragment {
	/**
	 * The fragment argument representing the item ID that this fragment
	 * represents.
	 */
	public static final String ARG_ITEM_ID = "item_id";

	/**
	 * The List content this fragment is presenting.
	 */
	private Task taskItem;

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public TaskDetailFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (getArguments().containsKey("Task")) {
			taskItem = this.getArguments().getParcelable("Task");
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_task_detail,
				container, false);
		// Show the List content as text in a TextView.
		if (taskItem != null) {
			((TextView) rootView.findViewById(R.id.task_name))
					.setText(taskItem.getName());
			((TextView) rootView.findViewById(R.id.task_assignee))
				.setText(taskItem.getAssignedUserId());
			((TextView) rootView.findViewById(R.id.task_hours_complete))
				.setText("" + taskItem.getHoursSpent() + " / " + taskItem.getCurrentHoursEstimate() + " hours");
			((TextView) rootView.findViewById(R.id.task_description))
				.setText(this.taskItem.getDescription());
			
			((TextView) rootView.findViewById(R.id.task_hours_original_estimate))
				.setText("Original Estimate: " + this.taskItem.getOriginalHoursEstimate() + " hours");
			((TextView) rootView.findViewById(R.id.task_hours_current_estimate))
				.setText("Current Estimate: " + this.taskItem.getCurrentHoursEstimate() + " hours");
		
		}

		return rootView;
	}
}

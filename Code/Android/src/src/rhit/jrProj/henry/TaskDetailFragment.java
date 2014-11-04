package rhit.jrProj.henry;

import rhit.jrProj.henry.firebase.Task;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

//import com.example.firebasetest.List.ListContent;

/**
 * A fragment representing a single Item detail screen. This fragment is either
 * contained in a {@link ItemListActivity} in two-pane mode (on tablets) or a
 * {@link ItemDetailActivity} on handsets.
 */
public class TaskDetailFragment extends Fragment implements
		OnItemSelectedListener {
	/**
	 * The fragment argument representing the item ID that this fragment
	 * represents.
	 */

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
			this.taskItem = this.getArguments().getParcelable("Task");
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_task_detail,
				container, false);
		// Show the List content as text in a TextView.
		if (this.taskItem != null) {
			((TextView) rootView.findViewById(R.id.task_name))
					.setText(this.taskItem.getName());
			
			((TextView) rootView.findViewById(R.id.task_assignee))
					.setText(this.taskItem.getAssignedUserName() +
							" - +" + this.taskItem.getAddedLines() + "/" + 
							"-" + this.taskItem.getRemovedLines() + " lines of code");
			
			((TextView) rootView.findViewById(R.id.task_hours_complete))
					.setText("" + this.taskItem.getHoursSpent() + " / "
							+ this.taskItem.getCurrentHoursEstimate()
							+ " hours");
			((TextView) rootView.findViewById(R.id.task_description))
					.setText(this.taskItem.getDescription());

			// //////
			// Task status spinner
			Spinner spinner = (Spinner) rootView
					.findViewById(R.id.task_status_spinner);
			// Create an ArrayAdapter using the string array and a default
			// spinner layout
			ArrayAdapter<CharSequence> adapter = ArrayAdapter
					.createFromResource(this.getActivity(),
							R.array.task_statuses,
							android.R.layout.simple_spinner_item);
			// Specify the layout to use when the list of choices appears
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			// Apply the adapter to the spinner
			spinner.setAdapter(adapter);

			// Set the default for the spinner to be the task's current status
			String myString = this.taskItem.getStatus();
			int spinnerDefaultPos = adapter.getPosition(myString);
			spinner.setSelection(spinnerDefaultPos);

			spinner.setOnItemSelectedListener(this);
			// /////

			((TextView) rootView
					.findViewById(R.id.task_hours_original_estimate))
					.setText("Original Estimate: "
							+ this.taskItem.getOriginalHoursEstimate()
							+ " hours");
			((TextView) rootView.findViewById(R.id.task_hours_current_estimate))
					.setText("Current Estimate: "
							+ this.taskItem.getCurrentHoursEstimate()
							+ " hours");

		}

		return rootView;
	}

	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		String taskStatus = (String) parent.getItemAtPosition(position);
		this.taskItem.updateStatus(taskStatus);
	}

	public void onNothingSelected(AdapterView<?> parent) {
		// do nothing
	}
}

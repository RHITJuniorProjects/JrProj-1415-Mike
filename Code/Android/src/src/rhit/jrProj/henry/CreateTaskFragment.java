package rhit.jrProj.henry;

import com.firebase.client.Firebase;

import android.app.DialogFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class CreateTaskFragment extends DialogFragment {

	/*
	 * Name of the Task
	 */
	private EditText mNameField;
	/*
	 * \ Description of the Task
	 */
	private EditText mDescriptionField;
	/*
	 * Milestone id associated with the Task
	 */
	private String milestoneId;

	/*
	 * Project id associated with the task
	 */
	private String projectId;

	/**
	 * Create a new instance of MyDialogFragment, providing "num" as an
	 * argument.
	 */
	public static CreateMilestoneFragment newInstance(String projectid) {
		CreateMilestoneFragment f = new CreateMilestoneFragment();

		// Supply num input as an argument.
		Bundle args = new Bundle();
		args.putString("projectid", projectid);
		f.setArguments(args);

		return f;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.milestoneId = this.getArguments().getString("milestoneId");
		this.projectId = this.getArguments().getString("projectId");

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		getDialog().setTitle("New Task");

		View v = inflater.inflate(R.layout.fragment_task_create, container,
				false);

		// Watch for button clicks.
		Button addTask = (Button) v.findViewById(R.id.TaskAddButton);
		addTask.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				createTask();
				CreateTaskFragment.this.dismiss();
			}
		});

		Button cancel = (Button) v.findViewById(R.id.TaskCancelButton);
		cancel.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				CreateTaskFragment.this.dismiss();
			}
		});

		this.mNameField = (EditText) v.findViewById(R.id.taskNameField);
		this.mNameField
				.setOnEditorActionListener(new TextView.OnEditorActionListener() {
					public boolean onEditorAction(TextView textView, int id,
							KeyEvent keyEvent) {
						if (id == R.id.taskNameField
								|| id == EditorInfo.IME_NULL) {
							return true;
						}
						return false;
					}
				});

		this.mDescriptionField = (EditText) v
				.findViewById(R.id.TaskDescriptionField);
		this.mDescriptionField
				.setOnEditorActionListener(new TextView.OnEditorActionListener() {
					public boolean onEditorAction(TextView textView, int id,
							KeyEvent keyEvent) {
						if (id == R.id.TaskDescriptionField
								|| id == EditorInfo.IME_NULL) {
							return true;
						}
						return false;
					}
				});
		return v;
	}

	/**
	 * Initializes values for a new task in Firebase.
	 */
	private void createTask() {

		String name = this.mNameField.getText().toString();
		String des = this.mDescriptionField.getText().toString();

		Firebase newTask = new Firebase(MainActivity.firebaseUrl + "projects/"
				+ this.projectId + "/milestones/" + this.milestoneId + "/tasks/").push();
		
		String newId = newTask.getName();
		Firebase newName = new Firebase(MainActivity.firebaseUrl + "projects/"
				+ this.projectId + "/milestones/" + this.milestoneId + "/tasks/" + newId
				+ "/name/");
		newName.setValue(name);
		Firebase newDes = new Firebase(MainActivity.firebaseUrl + "projects/"
				+ this.projectId + "/milestones/" + this.milestoneId + "/tasks/" + newId
				+ "/description/");
		newDes.setValue(des);

	}
}
package rhit.jrProj.henry;

import java.util.ArrayList;

import rhit.jrProj.henry.TaskDetailFragment.StatusSpinnerListener;
import rhit.jrProj.henry.TaskListFragment.Callbacks;
import rhit.jrProj.henry.firebase.Milestone;
import rhit.jrProj.henry.firebase.Project;
import rhit.jrProj.henry.firebase.Task;
import rhit.jrProj.henry.firebase.User;

import com.firebase.client.Firebase;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class CreateTaskFragment extends DialogFragment {
	/**
	 * The fragment's current callback object, which is notified of list item
	 * clicks.
	 */
	private Callbacks mCallbacks = sDummyCallbacks;
	/*
	 * Name of the Task
	 */
	private EditText mNameField;
	/*
	 * \ Description of the Task
	 */
	private EditText mDescriptionField;
	/*
	 * \ Category of the Task
	 */
	private Spinner mCategory;
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
	public static CreateTaskFragment newInstance(String projectid) {
		CreateTaskFragment f = new CreateTaskFragment();

		// Supply num input as an argument.
		Bundle args = new Bundle();
		args.putString("projectid", projectid);
		f.setArguments(args);

		return f;
	}
	/**
	 * A callback interface that all activities containing this fragment must
	 * implement. This mechanism allows activities to be notified of item
	 * selections.
	 */
	public interface Callbacks {
		/**
		 * Callback for when an item has been selected.
		 */
		public User getUser();

		public Milestone getSelectedMilestone();
		
	}
	/**
	 * A dummy implementation of the {@link Callbacks} interface that does
	 * nothing. Used only when this fragment is not attached to an activity.
	 */
	private static Callbacks sDummyCallbacks = new Callbacks() {

		public User getUser() {
			return null;
		}
		public Milestone getSelectedMilestone(){
			return null;
		}
		
		
		
	};

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

				String name = CreateTaskFragment.this.mNameField.getText()
						.toString();
				String des = CreateTaskFragment.this.mDescriptionField
						.getText().toString();
				String cat= CreateTaskFragment.this.mCategory.getSelectedItem().toString();
				boolean create = true;

				// Check for a valid description, if the user entered one.
				if (TextUtils.isEmpty(des)) {
					showErrorDialog(getString(R.string.invalidTaskDescription));
					create = false;
				}
				// Check for a valid name, if the user entered one. 
				else if (TextUtils.isEmpty(name)) {
					showErrorDialog(getString(R.string.invalidTaskName));
					create = false;
				}
				else if (TextUtils.isEmpty(name)) {
					showErrorDialog(getString(R.string.invalidTaskName));
					create = false;
				}
				else if (TextUtils.isEmpty(cat)) {
					showErrorDialog(getString(R.string.invalidTaskCategory));
					create = false;
				}
				if (create) {
					createTask();
					CreateTaskFragment.this.dismiss();
				} 
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
		// Task status spinner
					this.mCategory = (Spinner) v
							.findViewById(R.id.taskCategorySpinner);
					// Create an ArrayAdapter using the string array and a default
					// spinner layout
					ArrayAdapter<CharSequence> adapter = ArrayAdapter
							.createFromResource(this.getActivity(),
									R.array.task_categories,
									android.R.layout.simple_spinner_item);
					// Specify the layout to use when the list of choices appears
					adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					// Apply the adapter to the spinner
					mCategory.setAdapter(adapter);
					
					// Set the default for the spinner to be the task's current status
					
		this.mNameField.requestFocus();
		
		return v;
	}
	
	/**
	 * Opens a dialog window that displays the given error message. 
	 * @param message
	 */
	private void showErrorDialog(String message) {
		new AlertDialog.Builder(this.getActivity()).setTitle("Error").setMessage(message)
				.setPositiveButton(android.R.string.ok, null)
				.setIcon(android.R.drawable.ic_dialog_alert).show();
	}

	/**
	 * Initializes values for a new task in Firebase.
	 */
	private void createTask() {

		String name = this.mNameField.getText().toString();
		String des = this.mDescriptionField.getText().toString();
		String category=this.mCategory.getSelectedItem().toString();
		String user=new Firebase(MainActivity.firebaseUrl).getAuth().getUid()
				.toString();

		Firebase newTask = new Firebase(MainActivity.firebaseUrl + "projects/"
				+ this.projectId + "/milestones/" + this.milestoneId + "/tasks/").push();
//		newTask.child("name").setValue(name);
//		newTask.child("description").setValue(des);
//		newTask.child("category").setValue(category);
//		newTask.child("assignedTo").setValue(user);
//		newTask.child("due_date").setValue("No Due Date");
//		newTask.child("original_time_estimate").setValue(0);
//		if (this.mCallbacks.getSelectedMilestone()!=null){
//			this.mCallbacks.getSelectedMilestone().getTaskListViewCallback().onChange();
//		}
	}
}
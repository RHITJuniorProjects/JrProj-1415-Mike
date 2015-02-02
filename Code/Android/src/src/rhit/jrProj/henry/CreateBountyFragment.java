package rhit.jrProj.henry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import rhit.jrProj.henry.TaskListFragment.Callbacks;
import rhit.jrProj.henry.firebase.Bounty;
import rhit.jrProj.henry.firebase.Milestone;
import rhit.jrProj.henry.firebase.Project;
import rhit.jrProj.henry.firebase.Task;
import rhit.jrProj.henry.firebase.User;
import rhit.jrProj.henry.helpers.GeneralAlgorithms;
import rhit.jrProj.henry.helpers.HorizontalPicker;

import com.firebase.client.Firebase;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class CreateBountyFragment extends DialogFragment implements
		HorizontalPicker.Callbacks{
	/**
	 * The fragment's current callback object, which is notified of list item
	 * clicks.
	 */
	private Callbacks mCallbacks = sDummyCallbacks;
	private DatePicker mDatePicker;
	private Button choose;
	
	/*
	 * Name of the Bounty
	 */
	private EditText mNameField;
	/*
	 * \ Description of the Bounty
	 */
	private EditText mDescriptionField;
	/*
	 * \ Category of the Bounty
	 */
	private Spinner mCategory;
	/*
	 * Milestone id associated with the Bounty
	 */
	private String milestoneId;
	/*
	 * Milestone id associated with the Bounty
	 */
	private TextView mDueDate;

	/*
	 * Project id associated with the Bounty
	 */
	private String projectId;
	
	private String dueDate;
	private boolean hasDueDate=false;
	/*
	 * Points for the Bounty
	 */
	private HorizontalPicker mPointsField;
	private HorizontalPicker mHoursField;
	private HorizontalPicker mLinesField;
	private GlobalVariables mGlobalVariables;

	/**
	 * Create a new instance of MyDialogFragment, providing "num" as an
	 * argument.
	 */
	public static CreateBountyFragment newInstance(String projectid) {
		CreateBountyFragment f = new CreateBountyFragment();

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

		public Milestone getSelectedMilestone() {
			return null;
		}

	};
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mGlobalVariables =  ((GlobalVariables) getActivity().getApplicationContext());
		this.milestoneId = this.getArguments().getString("milestoneId");
		this.projectId = this.getArguments().getString("projectId");

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		getDialog().setTitle("New Bounty");

		View v = inflater.inflate(R.layout.fragment_bounty_create, container,
				false);
		mDatePicker=(DatePicker)v.findViewById(R.id.datePicker1);
		mDatePicker.setVisibility(View.GONE);

		// Watch for button clicks.
		Button addBounty = (Button) v.findViewById(R.id.BountyAddButton);
		addBounty.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				String name = CreateBountyFragment.this.mNameField.getText()
						.toString();
				String des = CreateBountyFragment.this.mDescriptionField
						.getText().toString();
				String cat = CreateBountyFragment.this.mCategory
						.getSelectedItem().toString();
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
				} else if (TextUtils.isEmpty(name)) {
					showErrorDialog(getString(R.string.invalidTaskName));
					create = false;
				} else if (TextUtils.isEmpty(cat)) {
					showErrorDialog(getString(R.string.invalidTaskCategory));
					create = false;
				}
				if (create) {
					createBounty();
					CreateBountyFragment.this.dismiss();
				}
			}
		});

		Button cancel = (Button) v.findViewById(R.id.BountyCancelButton);
		cancel.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				CreateBountyFragment.this.dismiss();
			}
		});
		choose= (Button) v.findViewById(R.id.button1);
		choose.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				if (!hasDueDate){
					showDatePicker();
				}
				else{
					hideDatePicker();
				}
				
			}
			
		});
		this.mDueDate = (TextView) v.findViewById(R.id.textView6);
		this.mNameField = (EditText) v.findViewById(R.id.bountyNameField);
		this.mNameField
				.setOnEditorActionListener(new TextView.OnEditorActionListener() {
					public boolean onEditorAction(TextView textView, int id,
							KeyEvent keyEvent) {
						if (id == R.id.bountyNameField
								|| id == EditorInfo.IME_NULL) {
							return true;
						}
						return false;
					}
				});

		this.mDescriptionField = (EditText) v
				.findViewById(R.id.BountyDescriptionField);
		this.mDescriptionField
				.setOnEditorActionListener(new TextView.OnEditorActionListener() {
					public boolean onEditorAction(TextView textView, int id,
							KeyEvent keyEvent) {
						if (id == R.id.BountyDescriptionField
								|| id == EditorInfo.IME_NULL) {
							return true;
						}
						return false;
					}
				});
		

		mPointsField = (HorizontalPicker) v
				.findViewById(R.id.horizontal_number_picker);
		System.out.println(Task.MAX_POINTS);
		mPointsField.setMaxValue(Task.MAX_POINTS);
		mPointsField.setMinValue(Task.MIN_POINTS);
		mPointsField.setCallbacks(this);
		mPointsField.setVisibility(View.VISIBLE);
		mPointsField.setEnabled(true);
		mPointsField.setValue(0);
		
		mHoursField = (HorizontalPicker) v
				.findViewById(R.id.horizontal_number_picker_hours);
		
		mHoursField.setMaxValue(100);
		mHoursField.setMinValue(0);
		mHoursField.setCallbacks(this);
		mHoursField.setVisibility(View.VISIBLE);
		mHoursField.setEnabled(true);
		mHoursField.setValue(0);
		
		mLinesField = (HorizontalPicker) v
				.findViewById(R.id.horizontal_number_picker_lines);
		mLinesField.setMaxValue(10000);
		mLinesField.setMinValue(0);
		mLinesField.setCallbacks(this);
		mLinesField.setVisibility(View.VISIBLE);
		mLinesField.setEnabled(true);
		mLinesField.setValue(0);

		this.mNameField.requestFocus();

		return v;
	}

	protected void hideDatePicker() {
		this.mDatePicker.setVisibility(View.GONE);
		this.hasDueDate=false;
		this.choose.setText("Choose");
		
	}

	protected void showDatePicker() {
		InputMethodManager imm = (InputMethodManager)this.getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(mNameField.getWindowToken(), 0);
		if (mNameField.hasFocus()){
			mNameField.clearFocus();
		}
		this.mDatePicker.setVisibility(View.VISIBLE);
		this.hasDueDate=true;
		this.choose.setText("No Due Date");
		
	}

	/**
	 * Opens a dialog window that displays the given error message.
	 * 
	 * @param message
	 */
	private void showErrorDialog(String message) {
		new AlertDialog.Builder(this.getActivity()).setTitle("Error")
				.setMessage(message)
				.setPositiveButton(android.R.string.ok, null)
				.setIcon(android.R.drawable.ic_dialog_alert).show();
	}

	/**
	 * Initializes values for a new bounty in Firebase.
	 */
	private void createBounty() {
		String s= "No Due Date";
		if (hasDueDate){
			s=mDatePicker.getYear()+"-"+(mDatePicker.getMonth()+1)+"-"+mDatePicker.getDayOfMonth();
		}
		
//		String name = this.mNameField.getText().toString();
//		String des = this.mDescriptionField.getText().toString();
//		String category = this.mCategory.getSelectedItem().toString();
//		String user = new Firebase(mGlobalVariables.getFirebaseUrl()).getAuth().getUid()
//				.toString();
//		Map<String, Object> map = new HashMap<String, Object>();
//		map.put("name", name);
//		map.put("description", des);
//		map.put("category", category);
//		map.put("due_date", "No Due Date");
//		map.put("assignedTo", user);
//		map.put("original_hour_estimate", 0);
//		map.put("points", this.mPointsField.getValue());
//
//		Firebase f2 = new Firebase(mGlobalVariables.getFirebaseUrl() + "projects/"
//				+ this.projectId + "/milestones/" + this.milestoneId
//				+ "/tasks/").push();
//		String id = f2.toString().substring(f2.toString().lastIndexOf("/") + 1);
//		new Firebase(mGlobalVariables.getFirebaseUrl() + "projects/" + this.projectId
//				+ "/milestones/" + this.milestoneId + "/tasks/" + id).setValue(map);
//
//		// Create bounties:
//		Map<String, Object> bounties = new HashMap<String, Object>();
//		bounties.put("claimed", "None");
//		bounties.put("description", "get points");
//		bounties.put("due_date", "No Due Date");
//		bounties.put("hour_limit", 50);
//		bounties.put("line_limit", "None");
//		bounties.put("name", Bounty.completionName);
//		bounties.put("points", mPointsField.getValue());
//		Firebase f3 = new Firebase(mGlobalVariables.getFirebaseUrl() + "projects/"
//				+ this.projectId + "/milestones/" + this.milestoneId + "/tasks/" + id + "/bounties/").push();
//		String id2 = f3.toString().substring(f3.toString().lastIndexOf("/") + 1);
//		new Firebase(mGlobalVariables.getFirebaseUrl() + "projects/" + this.projectId
//				+ "/milestones/" + this.milestoneId + "/tasks/" + id + "/bounties/" + id2).setValue(bounties);
		
	}

	@Override
	public void fireChange(int old, int nu) {
		// TODO Auto-generated method stub

	}
	
}
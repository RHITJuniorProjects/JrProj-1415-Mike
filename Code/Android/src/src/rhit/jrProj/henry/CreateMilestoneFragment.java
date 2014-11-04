package rhit.jrProj.henry;

import com.firebase.client.Firebase;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class CreateMilestoneFragment extends DialogFragment {

	/*
	 * Name of the milestone
	 */
	private EditText mNameField;
	/*
	 * \ Description of the milestone
	 */
	private EditText mDescriptionField;
	/*
	 * Project id associated with the milestone
	 */
	private String projectid;
	/*
	 * user id fror logged in user
	 */
	private String userid;

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
		this.projectid = this.getArguments().getString("projectid");
		userid = new Firebase(MainActivity.firebaseUrl).getAuth().getUid()
				.toString();

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		getDialog().setTitle("New Milestone");

		View v = inflater.inflate(R.layout.fragment_milestone_create,
				container, false);

		// Watch for button clicks.
		Button addMilestone = (Button) v.findViewById(R.id.MilestoneAddButton);
		addMilestone.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				createMilestone();
				CreateMilestoneFragment.this.dismiss();
			}
		});

		Button cancel = (Button) v.findViewById(R.id.MilestoneCancelButton);
		cancel.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				CreateMilestoneFragment.this.dismiss();
			}
		});

		this.mNameField = (EditText) v.findViewById(R.id.milestoneNameField);
		this.mNameField
				.setOnEditorActionListener(new TextView.OnEditorActionListener() {
					public boolean onEditorAction(TextView textView, int id,
							KeyEvent keyEvent) {
						if (id == R.id.milestoneNameField
								|| id == EditorInfo.IME_NULL) {
							return true;
						}
						return false;
					}
				});

		this.mDescriptionField = (EditText) v
				.findViewById(R.id.milestoneDescriptionField);
		this.mDescriptionField
				.setOnEditorActionListener(new TextView.OnEditorActionListener() {
					public boolean onEditorAction(TextView textView, int id,
							KeyEvent keyEvent) {
						if (id == R.id.milestoneDescriptionField
								|| id == EditorInfo.IME_NULL) {
							return true;
						}
						return false;
					}
				});
		return v;
	}

	/**
	 * Initializes values for a new milestone in Firebase.
	 */
	private void createMilestone() {
		String name = this.mNameField.getText().toString();
		String des = this.mDescriptionField.getText().toString();
		Firebase f = new Firebase(MainActivity.firebaseUrl + "projects/"
				+ this.projectid + "/milestones/").push();
		String newId = f.getName();
		Firebase f2 = new Firebase(MainActivity.firebaseUrl + "projects/"
				+ this.projectid + "/milestones/" + newId + "/name/");
		f2.setValue(name);
		Firebase f3 = new Firebase(MainActivity.firebaseUrl + "projects/"
				+ this.projectid + "/milestones/" + newId + "/description/");
		f3.setValue(des);

	}
}
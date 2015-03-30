package rhit.jrProj.henry;

import java.util.HashMap;
import java.util.Map;

import rhit.jrProj.henry.CreateTaskFragment.Callbacks;
import rhit.jrProj.henry.firebase.Milestone;
import rhit.jrProj.henry.firebase.Project;
import rhit.jrProj.henry.firebase.User;

import com.firebase.client.Firebase;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.text.TextUtils;
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
    private Callbacks mCallbacks = sDummyCallbacks;
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
    private GlobalVariables mGlobalVariables;

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

    public interface Callbacks{
        /**
         * Callback for when an item has been selected.
         */
        public User getUser();

        public Project getSelectedProject();

    }

    /**
     * A dummy implementation of the {@link Callbacks} interface that does
     * nothing. Used only when this fragment is not attached to an activity.
     */
    private static Callbacks sDummyCallbacks = new Callbacks() {

        public User getUser() {
            return null;
        }

        public Project getSelectedProject() {
            return null;
        }


    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGlobalVariables = ((GlobalVariables) getActivity().getApplicationContext());
        this.projectid = this.getArguments().getString("projectid");
        userid = new Firebase(mGlobalVariables.getFirebaseUrl()).getAuth().getUid()
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

                String name = CreateMilestoneFragment.this.mNameField.getText()
                        .toString();
                String des = CreateMilestoneFragment.this.mDescriptionField
                        .getText().toString();

                boolean create = true;

                // Check for a valid description, if the user entered one.
                if (TextUtils.isEmpty(des)) {
                    showErrorDialog(getString(R.string.invalidMilestoneDescription));
                    create = false;
                }
                // Check for a valid name, if the user entered one.
                else if (TextUtils.isEmpty(name)) {
                    showErrorDialog(getString(R.string.invalidMilestoneName));
                    create = false;
                }
                if (create) {
                    createMilestone();
                    CreateMilestoneFragment.this.dismiss();
                }
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
        this.mNameField.requestFocus();
        return v;
    }

    /**
     * Opens a dialog window that displays the given error message.
     *
     * @param message
     */
    private void showErrorDialog(String message) {
        new AlertDialog.Builder(this.getActivity()).setTitle("Error").setMessage(message)
                .setPositiveButton(android.R.string.ok, null)
                .setIcon(android.R.drawable.ic_dialog_alert).show();
    }

    /**
     * Initializes values for a new milestone in Firebase.
     */
    private void createMilestone() {
        String name = this.mNameField.getText().toString();
        String des = this.mDescriptionField.getText().toString();

        Firebase ref = new Firebase(GlobalVariables.getFirebaseUrl() + "projects/"
                + this.projectid + "/milestones/").push();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("name", name);
        map.put("description", des);
        map.put("due_date", "No Due Date");
        ref.setValue(map);

    }
}
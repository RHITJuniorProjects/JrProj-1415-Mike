package rhit.jrProj.henry;

import rhit.jrProj.henry.firebase.Bounty;
import rhit.jrProj.henry.firebase.Enums;
import rhit.jrProj.henry.firebase.Map;
import rhit.jrProj.henry.firebase.Member;
import rhit.jrProj.henry.firebase.Project;
import rhit.jrProj.henry.firebase.Task;
import rhit.jrProj.henry.firebase.User;
import rhit.jrProj.henry.firebase.Enums.Role;
import rhit.jrProj.henry.helpers.HorizontalPicker;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

/**
 * A fragment representing a single Bounty detail screen. This fragment is
 * either contained in a  in two-pane mode (on
 * tablets) or a  on handsets.
 */
public class BountyDetailFragment extends Fragment implements
        HorizontalPicker.Callbacks {


    /**
     * The dummy content this fragment is presenting.
     */
    private Bounty bountyItem;
    private Callbacks mCallbacks = sDummyCallbacks;

    private TextView pointsField;


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public BountyDetailFragment() {
    }

    public interface Callbacks {
        public Map<Member, Enums.Role> getProjectMembers();

        public Bounty getSelectedBounty();

        public Project getSelectedProject();

        public User getUser();
    }

    private static Callbacks sDummyCallbacks = new Callbacks() {

        public Map<Member, Role> getProjectMembers() {
            return null;
        }

        public Project getSelectedProject() {
            return null;
        }

        public Bounty getSelectedBounty() {
            return null;
        }

        public User getUser() {
            return null;
        }

    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(
                R.layout.fragment_bounty_detail, container, false);
        this.bountyItem = this.mCallbacks.getSelectedBounty();
        if (this.bountyItem != null) {
            ((TextView) rootView.findViewById(R.id.bounty_name))
                    .setText(this.bountyItem.getName());
            ((TextView) rootView.findViewById(R.id.bounty_due_date))
                    .setText("Due: "
                            + this.bountyItem.getDueDateFormatted());
            ((TextView) rootView.findViewById(R.id.bounty_description))
                    .setText("Description: "
                            + this.bountyItem.getDescription());
            this.pointsField = ((TextView) rootView
                    .findViewById(R.id.bounty_points));
            this.pointsField.setText("Points: \t"
                    + this.bountyItem.getPoints());
            HorizontalPicker numPicker = (HorizontalPicker) rootView
                    .findViewById(R.id.horizontal_number_picker);
//			numPicker.setValue(this.bountyItem.getPoints());
            numPicker.setMaxValue(Task.MAX_POINTS);
            numPicker.setMinValue(Task.MIN_POINTS);
            numPicker.setCallbacks(this);
//			if (!this.getArguments().getBoolean("TwoPane")){
//				numPicker.hideButtons();
//			}
//			numPicker.setBounty(this.bountyItem);
            if (mCallbacks.getSelectedProject().isLead(mCallbacks.getUser()) && this.getArguments().getBoolean("TwoPane")) {
                numPicker.setVisibility(View.VISIBLE);
                numPicker.setEnabled(true);


            }
            numPicker.setValue(this.bountyItem.getPoints());

//			((TextView) rootView.findViewById(R.id.bounty_task_percent))
//					.setText("Tasks Completed: "
//							+ this.bountyItem.getTaskPercent() + "%");
//			ProgressBar taskCompleteBar = ((ProgressBar) rootView
//					.findViewById(R.id.bounty_task_progress_bar));
//			taskCompleteBar.setMax(100);
//			taskCompleteBar.setProgress(this.bountyItem.getTaskPercent());

            // //////
            // Task status spinner

        }

        return rootView;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        // This code shows the "Create Task" option when
        // viewing tasks.
        MenuItem createBounty = menu.findItem(R.id.action_bounty);
        createBounty.setVisible(false);
        createBounty.setEnabled(false);

        MenuItem createTask = menu.findItem(R.id.action_task);
        createTask.setVisible(false);
        createTask.setEnabled(false);

    }

    public void onNothingSelected(AdapterView<?> parent) {
        // do nothing
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (!(activity instanceof Callbacks)) {
            throw new IllegalStateException(
                    "Activity must implement fragment's callbacks.");
        }
        this.mCallbacks = (Callbacks) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.mCallbacks = sDummyCallbacks;
    }


    @Override
    public void fireChange(int old, int nu) {
        this.bountyItem.setPoints(nu);
        if (this.bountyItem.isCompletion()) {
            this.bountyItem.setParentTaskPoints();
        }
        this.pointsField.setText("Points: \t" + nu);
    }


}

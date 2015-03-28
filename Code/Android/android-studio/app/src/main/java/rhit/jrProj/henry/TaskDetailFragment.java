package rhit.jrProj.henry;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import rhit.jrProj.henry.firebase.Enums;
import rhit.jrProj.henry.firebase.Enums.Role;
import rhit.jrProj.henry.firebase.Map;
import rhit.jrProj.henry.firebase.Member;
import rhit.jrProj.henry.firebase.Project;
import rhit.jrProj.henry.firebase.Task;
import rhit.jrProj.henry.firebase.User;
import rhit.jrProj.henry.helpers.HorizontalPicker;

/**
 * A fragment representing a single Item detail screen. This fragment is either
 * contained in a {ItemListActivity} in two-pane mode (on tablets) or a
 * {ItemDetailActivity} on handsets.
 */
public class TaskDetailFragment extends Fragment implements
        HorizontalPicker.Callbacks {

    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */

    /**
     * The List content this fragment is presenting.
     */
    private Task taskItem;

    private Callbacks mCallbacks = sDummyCallbacks;

    private TextView pointsField;

    private GlobalVariables mGlobalVariables;

    private HorizontalPicker numPicker;
    private Spinner spinny;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public TaskDetailFragment() {
    }

    public interface Callbacks{
        public Map<Member, Enums.Role> getProjectMembers();

        public Task getSelectedTask();

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

        public Task getSelectedTask() {
            return null;
        }

        public User getUser() {
            return null;
        }


    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mGlobalVariables = ((GlobalVariables) getActivity().getApplicationContext());
        this.taskItem = this.mCallbacks.getSelectedTask();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_task_detail,
                container, false);
        // Show the List content as text in a TextView.
        if (this.taskItem != null) {
//			this.taskItem.mCallbacks=this;
            ((TextView) rootView.findViewById(R.id.task_name))
                    .setText(this.taskItem.getName());
            Enums.Role role = this.mCallbacks
                    .getSelectedProject()
                    .getMembers()
                    .getValue(
                            new Member(mGlobalVariables.getFirebaseUrl()
                                    + "/users/"
                                    + this.mCallbacks.getUser().getKey()));
            if (this.getArguments().getBoolean("TwoPane")
                    && role == Enums.Role.LEAD) {

                ((TextView) rootView.findViewById(R.id.task_assignee))
                        .setText("Assignee:");


                spinny = (Spinner) rootView.findViewById(R.id.task_assignee_spinner);
                this.mCallbacks.getProjectMembers().getAllKeys();

                ArrayAdapter<Member> adapter = new ArrayAdapter<Member>(
                        this.getActivity(),
                        android.R.layout.simple_spinner_item, this.mCallbacks
                        .getProjectMembers().getAllKeys());
                // Specify the layout to use when the list of choices appears
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                // Apply the adapter to the spinner
                spinny.setAdapter(adapter);

                int spinnerDefaultPos = adapter.getPosition(new Member(
                        mGlobalVariables.getFirebaseUrl() + "/users/"
                                + this.taskItem.getAssignedUserId()));
                spinny.setSelection(spinnerDefaultPos);
                spinny.setOnItemSelectedListener(new AssigneeSpinnerListener(
                        this.taskItem));
                spinny.setVisibility(View.VISIBLE);
                TextView textLines = (TextView) rootView.findViewById(R.id.task_lines_of_code);
                textLines.setText(this.taskItem.getAddedLines() + "/" + "-"
                        + this.taskItem.getRemovedLines() + " lines of code");
            } else {
                rootView.findViewById(R.id.task_assignee_spinner).setVisibility(View.GONE);
                ((TextView) rootView.findViewById(R.id.task_assignee))
                        .setText("Assigned to: "
                                + this.taskItem.getAssignedUserName() + " \n +"
                                + this.taskItem.getAddedLines() + "/" + "-"
                                + this.taskItem.getRemovedLines()
                                + " lines of code");
            }
            this.pointsField = ((TextView) rootView
                    .findViewById(R.id.task_points));
            this.pointsField.setText("Points: \t"
                    + this.taskItem.getPoints());
            ((TextView) rootView.findViewById(R.id.task_hours_complete))
                    .setText("" + this.taskItem.getHoursSpent() + " / "
                            + this.taskItem.getCurrentHoursEstimate()
                            + " hours");
            ((TextView) rootView.findViewById(R.id.task_description))
                    .setText("Description: " + this.taskItem.getDescription());

            ((TextView) rootView.findViewById(R.id.status_descriptor))
                    .setText("Category:");

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
            spinner.setOnItemSelectedListener(new StatusSpinnerListener(
                    this.taskItem));

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
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        // This code shows the "Create Task" option when
        // viewing tasks.
        MenuItem createMilestone = menu.findItem(R.id.action_milestone);
        createMilestone.setVisible(false);
        createMilestone.setEnabled(false);

        MenuItem createTask = menu.findItem(R.id.action_task);
        createTask.setVisible(false);
        createTask.setEnabled(false);
        MenuItem sorting = menu.findItem(R.id.action_sorting);

        sorting.setEnabled(false);
        sorting.setVisible(false);
        MenuItem createbounty = menu.findItem(R.id.action_bounty);
        createbounty.setVisible(false);
        createbounty.setEnabled(false);
        MenuItem allTasks = menu.findItem(R.id.action_all_tasks);
        allTasks.setVisible(false);
        allTasks.setEnabled(false);

    }

    /**
     * Status Spinner Listener class
     */
    class StatusSpinnerListener implements OnItemSelectedListener {
        /**
         * The selected task item
         */
        private Task taskItem;

        /**
         * Creates a new Status Listener
         *
         * @param taskItem
         */
        public StatusSpinnerListener(Task taskItem) {
            this.taskItem = taskItem;
        }

        public void onItemSelected(AdapterView<?> parent, View view,
                                   int position, long id) {
            String taskStatus = parent.getItemAtPosition(position).toString();
            this.taskItem.updateStatus(taskStatus);
        }

        public void onNothingSelected(AdapterView<?> parent) {
            // do nothing
        }
    }

    /**
     * Status Spinner Listener class
     */
    class AssigneeSpinnerListener implements OnItemSelectedListener {
        /**
         * The selected task item
         */
        private Task taskItem;

        /**
         * Creates a new Status Listener
         *
         * @param taskItem
         */
        public AssigneeSpinnerListener(Task taskItem) {
            this.taskItem = taskItem;
        }

        public void onItemSelected(AdapterView<?> parent, View view,
                                   int position, long id) {
            Member member = (Member) parent.getItemAtPosition(position);
            this.taskItem.updateAssignee(member);
        }

        public void onNothingSelected(AdapterView<?> parent) {
            // do nothing
        }
    }

    @Override
    public void fireChange(int old, int nu) {
        this.taskItem.setCompletionBountyPoints(nu);
        this.pointsField.setText("Points: \t" + nu);
    }

}
